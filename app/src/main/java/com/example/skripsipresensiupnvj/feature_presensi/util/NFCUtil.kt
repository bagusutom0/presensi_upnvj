package com.example.skripsipresensiupnvj.feature_presensi.util

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.util.Log
import java.io.IOException
import java.nio.charset.Charset

@Suppress("DEPRECATION")
object NFCUtil {
    fun createNFCMessage(payload: String, intent: Intent?): Boolean {

        val pathPrefix = "/com.example:presensiUpnvjApp"
        val nfcRecord = NdefRecord(
            NdefRecord.TNF_EXTERNAL_TYPE,
            pathPrefix.toByteArray(),
            ByteArray(0),
            payload.toByteArray(Charset.forName("US-ASCII"))
        )
        val nfcMessage = NdefMessage(arrayOf(nfcRecord))
        intent?.let {
            val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            return writeMessageToTag(nfcMessage, tag)
        }
        return false
    }

    fun readNFCMessage(tag: Tag?): String {
        var result = "Terjadi kesalahan, coba lagi!"
        try {
            val nDefTag = Ndef.get(tag)

            nDefTag?.let {
                it.connect()
                Log.d("TAG", "readNFCMessage: muncul")
                result = "Ndef Record: " + it.ndefMessage.records[0].payload.toString(Charset.forName("US-ASCII"))
                it.close()
            }
        } catch (e: Exception) {
            Log.e("NFCUtil", "readNFCMessage: $e", )
        }

        return result
    }

    private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {

        try {
            val nDefTag = Ndef.get(tag)
            Log.d("TAG", "writeMessageToTag: muncul")
            nDefTag?.let {
                it.connect()
                if (it.maxSize < nfcMessage.toByteArray().size) {
                    it.close()
                    return false
                }
                if (it.isWritable) {
                    it.writeNdefMessage(nfcMessage)
                    it.close()
                    return true
                } else {
                    it.close()
                    return false
                }
            }

            val nDefFormatableTag = NdefFormatable.get(tag)

            nDefFormatableTag?.let {
                try {
                    it.connect()
                    it.format(nfcMessage)
                    it.close()
                    return true
                } catch (e: IOException) {
                    Log.e("NFCUtil", "writeMessageToTag: $e", )
                    return false
                }
            }
            return false

        } catch (e: Exception) {
            Log.e("NFCUtil", "writeMessageToTag: $e", )
        }
        return false
    }
}