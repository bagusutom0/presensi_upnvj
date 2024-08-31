@file:Suppress("DEPRECATION")

package com.example.skripsipresensiupnvj.feature_presensi.presentation.util

import android.content.Context
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.NavController
import com.example.skripsipresensiupnvj.feature_presensi.data.data_store.Session
import java.io.IOException
import java.nio.charset.Charset
import java.util.Locale
import javax.inject.Inject

class NfcHandler : LifecycleObserver {

    private val TAG = "NfcHandler"
    private var currentTag: Tag? = null
    private lateinit var currentContext: Context
    private lateinit var navigation: NavController
    @Inject lateinit var session: Session

    fun handleNfcTag(tag: Tag?, context: Context, navController: NavController) {
        currentTag = tag
        currentContext = context
        navigation = navController
    }

    // Lifecycle event handling
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        // Initialization code here
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanup() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    fun createTextRecord(payload: String, locale: Locale, encodeInUtf8: Boolean): NdefRecord {
        val langBytes = locale.language.toByteArray(Charset.forName("US-ASCII"))
        val utfEncoding = if (encodeInUtf8) Charset.forName("UTF-8") else Charset.forName("UTF-16")
        val textBytes = payload.toByteArray(utfEncoding)
        val utfBit: Int = if (encodeInUtf8) 0 else 1 shl 7
        val status = (utfBit + langBytes.size).toChar()
        val data = ByteArray(1 + langBytes.size + textBytes.size)
        data[0] = status.toByte()
        System.arraycopy(langBytes, 0, data, 1, langBytes.size)
        System.arraycopy(textBytes, 0, data, 1 + langBytes.size, textBytes.size)
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), data)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun writeNdefMessage(message: String, listener: NfcWriteListener) {
        if (currentTag != null) {
            val ndef = Ndef.get(currentTag)
            if (ndef != null) {
                try {
                    ndef.connect()
                    val extRecord = createTextRecord(message, Locale.ENGLISH, true)
                    ndef.writeNdefMessage(NdefMessage(arrayOf(extRecord)))
                    listener.onNfcWriteSuccess()
                } catch (e: IOException) {
                    listener.onNfcWriteError("Error writing NFC message: ${e.message}")
                } finally {
                    ndef.close()
                }
            } else {
                listener.onNfcWriteError("NDEF is not supported")
            }
        }
    }

    fun readNdefMessage(): String {
        var result = ""
        if (currentTag != null) {
            val ndef = Ndef.get(currentTag)
            if (ndef != null) {
                ndef.connect()
                val ndefMessage = ndef.ndefMessage
                for (record in ndefMessage.records) {
                    result = record.payload
                        .toString(Charset.forName("UTF-8"))
                        .substring(3)
                }
                ndef.close()
            }
        }
         return result
    }

    suspend fun processNdefMessage() {
        session.userFlow.collect {
            if (it?.username != "") {
                val ndefMessage = readNdefMessage()
                if (ndefMessage != "") {
                    navigation.navigate(
                        Screen.DetailKegiatanScreen.route +
                                "?idKegiatan=${ndefMessage}" +
                                "&username=${it?.username}&password=${it?.password}"
                    )
                }
            } else {
                Toast.makeText(currentContext, "Mohon Login terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}