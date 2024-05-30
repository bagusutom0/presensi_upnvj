package com.example.skripsipresensiupnvj.feature_presensi.domain.model

data class Kegiatan(
    val judul: String = "",
    val detail: String = "",
    val waktuMulai: String = "",
    val waktuSelesai: String = "",
    val tanggal: String = "",
    val lokasi: String = "",
    var isPublic: Boolean = true,
    val minCoordinate: String = "",
    val maxCoordinate: String = "",
)
