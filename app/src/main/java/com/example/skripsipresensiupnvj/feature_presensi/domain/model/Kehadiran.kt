package com.example.skripsipresensiupnvj.feature_presensi.domain.model

data class Kehadiran(
    val userId: String = "",
    val kegiatanId: String = "",
    val alasan: String = "",
    val presensiMasuk: String = "Tidak hadir",
    val presensiKeluar: String = "Tidak hadir"
)
