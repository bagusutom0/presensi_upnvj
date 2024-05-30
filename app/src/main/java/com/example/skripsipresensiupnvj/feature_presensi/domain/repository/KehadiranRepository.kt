package com.example.skripsipresensiupnvj.feature_presensi.domain.repository

import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kehadiran
import kotlinx.coroutines.flow.Flow

interface KehadiranRepository {
    suspend fun getKehadiran(
        username: String,
        password: String,
        judul: String,
        lokasi: String
    ): Flow<Resource<Kehadiran>>

    suspend fun submitAlasanKehadiran(
        idKegiatan: String,
        username: String,
        password: String,
        alasan: String
    )

    suspend fun presensiMasuk(username: String, password: String, judul: String, lokasi: String)
    suspend fun presensiKeluar(username: String, password: String, judul: String, lokasi: String)
}