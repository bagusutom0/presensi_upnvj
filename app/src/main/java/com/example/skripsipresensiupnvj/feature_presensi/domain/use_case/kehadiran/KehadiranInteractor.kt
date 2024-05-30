package com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kehadiran

import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kehadiran
import com.example.skripsipresensiupnvj.feature_presensi.domain.repository.KehadiranRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KehadiranInteractor @Inject constructor(
    private val kehadiranRepository: KehadiranRepository
): KehadiranUseCase {
    override suspend fun getKehadiran(
        username: String,
        password: String,
        judul: String,
        lokasi: String
    ): Flow<Resource<Kehadiran>> {
        return kehadiranRepository.getKehadiran(username, password, judul, lokasi)
    }

    override suspend fun submitAlasanKehadiran(
        idKegiatan: String,
        username: String,
        password: String,
        alasan: String
    ) {
        return kehadiranRepository.submitAlasanKehadiran(idKegiatan,username, password, alasan)
    }

    override suspend fun presensiMasuk(
        username: String,
        password: String,
        judul: String,
        lokasi: String
    ) {
        return kehadiranRepository.presensiMasuk(username, password, judul, lokasi)
    }

    override suspend fun presensiKeluar(
        username: String,
        password: String,
        judul: String,
        lokasi: String
    ) {
        return kehadiranRepository.presensiKeluar(username, password, judul, lokasi)
    }
}