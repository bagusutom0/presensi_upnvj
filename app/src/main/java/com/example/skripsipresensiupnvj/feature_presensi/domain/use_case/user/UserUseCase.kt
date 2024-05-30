package com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.user

import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    suspend fun getConfirmedUser(
        username: String,
        password: String,
        deviceId: String
    ): Flow<Resource<User>>
    suspend fun getUnconfirmedUser(): Flow<Resource<List<User>>>
    suspend fun getUser(username: String, password: String): Flow<Resource<User>>
    suspend fun inputUser(user: User)
    suspend fun confirmUser(user: User)
    suspend fun updateUser(username: String, password: String, user: User)
}