package com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kegiatan

import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kegiatan
import com.example.skripsipresensiupnvj.feature_presensi.domain.repository.KegiatanRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KegiatanInteractor @Inject constructor(
    private val kegiatanRepository: KegiatanRepository
): KegiatanUseCase {
    override suspend fun inputKegiatan(kegiatan: Kegiatan) {
        kegiatanRepository.inputKegiatan(kegiatan)
    }

    override suspend fun getListKegiatan(): Flow<Resource<List<Kegiatan>>> {
        return kegiatanRepository.getListKegiatan()
    }

    override suspend fun getKegiatan(judul: String, lokasi: String): Flow<Resource<Kegiatan>> {
        return kegiatanRepository.getKegiatan(judul, lokasi)
    }

    override suspend fun getIdKegiatan(judul: String, lokasi: String): Flow<Resource<String>> {
        return kegiatanRepository.getIdKegiatan(judul, lokasi)
    }

    override suspend fun getKegiatanById(id: String): Flow<Resource<Kegiatan>> {
        return kegiatanRepository.getKegiatanById(id)
    }

    override suspend fun getIdKegiatanFromCoordinate(minCoordinate: String, maxCoordinate: String): Flow<Resource<String>> {
        return kegiatanRepository.getIdKegiatanFromCoordinate(minCoordinate, maxCoordinate)
    }
}