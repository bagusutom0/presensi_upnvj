package com.example.skripsipresensiupnvj.feature_presensi.data.repository

import android.util.Log
import com.example.skripsipresensiupnvj.feature_presensi.data.data_source.FirestoreDataSource
import com.example.skripsipresensiupnvj.feature_presensi.data.data_source.FirestoreResponse
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kehadiran
import com.example.skripsipresensiupnvj.feature_presensi.domain.repository.KehadiranRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class KehadiranRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreDataSource
) : KehadiranRepository {
    override suspend fun getKehadiran(
        username: String,
        password: String,
        judul: String,
        lokasi: String
    ): Flow<Resource<Kehadiran>> {
        return channelFlow {
            trySend(Resource.Loading())
            when (
                val firestoreResponse =
                dataSource.getKehadiran(username, password, judul, lokasi).first()
            ) {
                is FirestoreResponse.Success -> {
                    trySend(Resource.Success(firestoreResponse.data))
                }

                is FirestoreResponse.Empty -> {
                    trySend(Resource.Error("Tidak ada Kehadiran!"))
                }

                is FirestoreResponse.Error -> {
                    trySend(Resource.Error(firestoreResponse.errorMessage))
                }

                else -> {
                    Log.e("KehadiranRepository", "getListKegiatan: null")
                }
            }
        }
    }

    override suspend fun submitAlasanKehadiran(
        idKegiatan: String,
        username: String,
        password: String,
        alasan: String
    ) {
        dataSource.submitAlasanKehadiran(idKegiatan, username, password, alasan)
    }

    override suspend fun presensiMasuk(
        username: String,
        password: String,
        judul: String,
        lokasi: String
    ) {
        dataSource.presensiMasuk(username, password, judul, lokasi)
    }

    override suspend fun presensiKeluar(
        username: String,
        password: String,
        judul: String,
        lokasi: String
    ) {
        dataSource.presensiKeluar(username, password, judul, lokasi)
    }
}