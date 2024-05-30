@file:Suppress("DEPRECATION")

package com.example.skripsipresensiupnvj.feature_presensi.presentation.util

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import java.io.IOException

class NfcHandler : LifecycleObserver {

    private val TAG = "NfcHandler"
    private var currentTag: Tag? = null

    fun handleNfcTag(tag: Tag?) {
        currentTag = tag
    }

    // Lifecycle event handling
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        // Initialization code here
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanup() {
        // Cleanup code here
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    // Function to write NDEF message to NFC tag
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun writeNdefMessage(message: String, listener: NfcWriteListener) {
        if (currentTag != null) {
            val ndef = Ndef.get(currentTag)
            if (ndef != null) {
                try {
                    ndef.connect()
                    val payload = message.toByteArray()
                    val domain = "com.example.skripsipresensiupnvj"
                    val type = "externalType"
                    val extRecord = NdefRecord.createExternal(domain, type, payload)
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

//    // Function to read NDEF message from NFC tag
//    fun readNdefMessage(): String {
//        var result = ""
//        if (currentTag != null) {
//            val ndef = Ndef.get(currentTag)
//            if (ndef != null) {
//                ndef.connect()
//                val ndefMessage = ndef.ndefMessage
//                for (record in ndefMessage.records) {
//                    if (record.tnf == NdefRecord.TNF_EXTERNAL_TYPE) {
//                        val payload = record.payload
//                        result = if (payload != null) {
//                            String(payload)
//                        } else {
//                            ""
//                        } // tinggal pindah ke detail kegiatan dari nfc
//                    }
//                }
//                ndef.close()
//            }
//        }
//         return result
//    }
}