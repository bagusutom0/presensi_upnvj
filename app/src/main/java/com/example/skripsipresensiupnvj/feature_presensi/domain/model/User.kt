package com.example.skripsipresensiupnvj.feature_presensi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String = "",
    val password: String = "",
    val jabatan: String = "",
    val nama: String = "",
    val isConfirmed: Boolean = false,
    val deviceId: String = "",
)