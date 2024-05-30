package com.example.skripsipresensiupnvj.feature_presensi.presentation.util

interface NfcWriteListener {
    fun onNfcWriteSuccess()
    fun onNfcWriteError(errorMessage: String)
}