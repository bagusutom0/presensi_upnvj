package com.example.skripsipresensiupnvj.feature_presensi.data.repository

import com.example.skripsipresensiupnvj.feature_presensi.data.data_source.FirestoreDataSource
import com.example.skripsipresensiupnvj.feature_presensi.data.data_source.FirestoreResponse
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kegiatan
import com.example.skripsipresensiupnvj.feature_presensi.domain.repository.KegiatanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class KegiatanRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreDataSource
) : KegiatanRepository {
    override suspend fun inputKegiatan(kegiatan: Kegiatan) {
        dataSource.inputKegiatan(kegiatan)
    }

    override suspend fun getListKegiatan(): Flow<Resource<List<Kegiatan>>> {
        return channelFlow {
            trySend(Resource.Loading())
            when (val firestoreResponse = dataSource.getListKegiatan().first()) {
                is FirestoreResponse.Success -> {
                    trySend(Resource.Success(firestoreResponse.data))
                }

                is FirestoreResponse.Empty -> {
                    trySend(Resource.Error("Tidak ada kegiatan!"))
                }

                is FirestoreResponse.Error -> {
                    trySend(Resource.Error(firestoreResponse.errorMessage))
                }

            }
        }
    }

    override suspend fun getKegiatan(judul: String, lokasi: String): Flow<Resource<Kegiatan>> {
        return channelFlow {
            trySend(Resource.Loading())
            when (val firestoreResponse = dataSource.getKegiatan(judul, lokasi).first()) {
                is FirestoreResponse.Success -> {
                    trySend(Resource.Success(firestoreResponse.data))
                }

                is FirestoreResponse.Empty -> {
                    trySend(Resource.Error("Tidak ada kegiatan!"))
                }

                is FirestoreResponse.Error -> {
                    trySend(Resource.Error(firestoreResponse.errorMessage))
                }
            }
        }
    }

    override suspend fun getIdKegiatan(judul: String, lokasi: String): Flow<Resource<String>> {
        return channelFlow {
            trySend(Resource.Loading())
            when (val firestoreResponse = dataSource.getIdKegiatan(judul, lokasi).first()) {
                is FirestoreResponse.Success -> {
                    trySend(Resource.Success(firestoreResponse.data))
                }

                is FirestoreResponse.Empty -> {
                    trySend(Resource.Error("Tidak ada kegiatan!"))
                }

                is FirestoreResponse.Error -> {
                    trySend(Resource.Error(firestoreResponse.errorMessage))
                }

            }
        }
    }

    override suspend fun getKegiatanById(id: String): Flow<Resource<Kegiatan>> {
        return channelFlow {
            trySend(Resource.Loading())
            when (val firestoreResponse = dataSource.getKegiatanById(id).first()) {
                is FirestoreResponse.Success -> {
                    trySend(Resource.Success(firestoreResponse.data))
                }

                is FirestoreResponse.Empty -> {
                    trySend(Resource.Error("Tidak ada kegiatan!"))
                }

                is FirestoreResponse.Error -> {
                    trySend(Resource.Error(firestoreResponse.errorMessage))
                }

            }
        }
    }

    override suspend fun getIdKegiatanFromCoordinate(minCoordinate: String, maxCoordinate: String): Flow<Resource<String>> {
        return channelFlow {
            trySend(Resource.Loading())
            when (val firestoreResponse =
                dataSource.getIdKegiatanFromCoordinate(minCoordinate, maxCoordinate).first()) {
                is FirestoreResponse.Success -> {
                    trySend(Resource.Success(firestoreResponse.data))
                }

                is FirestoreResponse.Empty -> {
                    trySend(Resource.Error("Tidak ada kegiatan!"))
                }

                is FirestoreResponse.Error -> {
                    trySend(Resource.Error(firestoreResponse.errorMessage))
                }

            }
        }
    }
}