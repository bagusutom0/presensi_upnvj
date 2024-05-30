package com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kegiatan

import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kegiatan
import kotlinx.coroutines.flow.Flow

interface KegiatanUseCase {
    suspend fun inputKegiatan(kegiatan: Kegiatan)
    suspend fun getListKegiatan(): Flow<Resource<List<Kegiatan>>>
    suspend fun getKegiatan(judul: String, lokasi: String): Flow<Resource<Kegiatan>>
    suspend fun getIdKegiatan(judul: String, lokasi: String): Flow<Resource<String>>
    suspend fun getKegiatanById(id: String): Flow<Resource<Kegiatan>>
    suspend fun getIdKegiatanFromCoordinate(minCoordinate: String, maxCoordinate: String): Flow<Resource<String>>
}