package com.example.skripsipresensiupnvj.feature_presensi.data.repository

import com.example.skripsipresensiupnvj.feature_presensi.data.data_source.FirestoreDataSource
import com.example.skripsipresensiupnvj.feature_presensi.data.data_source.FirestoreResponse
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.User
import com.example.skripsipresensiupnvj.feature_presensi.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreDataSource
) : UserRepository {

    override suspend fun getConfirmedUser(
        username: String,
        password: String,
        deviceId: String
    ): Flow<Resource<User>> {
        return channelFlow {
            trySend(Resource.Loading())
            when (val firestoreResponse = dataSource.getConfirmedUser(username, password, deviceId).first()) {
                is FirestoreResponse.Success -> {
                    trySend(Resource.Success(firestoreResponse.data))
                }

                is FirestoreResponse.Empty -> {
                    trySend(Resource.Error("Tidak ada pengguna terdaftar!"))
                }

                is FirestoreResponse.Error -> {
                    trySend(Resource.Error(firestoreResponse.errorMessage))
                }

            }
        }
    }

    override suspend fun getUser(username: String, password: String): Flow<Resource<User>> {
        return channelFlow {
            trySend(Resource.Loading())
            when (val firestoreResponse = dataSource.getUser(username, password).first()) {
                is FirestoreResponse.Success -> {
                    trySend(Resource.Success(firestoreResponse.data))
                }

                is FirestoreResponse.Empty -> {
                    trySend(Resource.Error("Tidak ada pengguna terdaftar!"))
                }

                is FirestoreResponse.Error -> {
                    trySend(Resource.Error(firestoreResponse.errorMessage))
                }

            }
        }
    }

    override suspend fun getUnconfirmedUser(): Flow<Resource<List<User>>> {
        return channelFlow {
            trySend(Resource.Loading())
            when (val firestoreResponse = dataSource.getUnconfirmedUser().first()) {
                is FirestoreResponse.Success -> {
                    trySend(Resource.Success(firestoreResponse.data))
                }

                is FirestoreResponse.Empty -> {
                    trySend(Resource.Error("Tidak ada pengguna terdaftar!"))
                }

                is FirestoreResponse.Error -> {
                    trySend(Resource.Error(firestoreResponse.errorMessage))
                }

            }
        }
    }

    override suspend fun inputUser(user: User) {
        dataSource.inputUser(user)
    }

    override suspend fun confirmUser(user: User) {
        dataSource.confirmUser(user)
    }

    override suspend fun updateUser(username: String, password: String, user: User) {
        dataSource.updateUser(username, password, user)
    }
}