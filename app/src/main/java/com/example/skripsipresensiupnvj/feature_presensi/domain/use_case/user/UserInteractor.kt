package com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.user

import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.User
import com.example.skripsipresensiupnvj.feature_presensi.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInteractor @Inject constructor(private val userRepository: UserRepository) : UserUseCase {
    override suspend fun getConfirmedUser(
        username: String,
        password: String,
        deviceId: String
    ): Flow<Resource<User>> {
        return userRepository.getConfirmedUser(username, password, deviceId)
    }

    override suspend fun getUnconfirmedUser(): Flow<Resource<List<User>>> {
        return userRepository.getUnconfirmedUser()
    }

    override suspend fun getUser(username: String, password: String): Flow<Resource<User>> {
        return userRepository.getUser(username, password)
    }

    override suspend fun inputUser(user: User) {
        return userRepository.inputUser(user)
    }

    override suspend fun confirmUser(user: User) {
        userRepository.confirmUser(user)
    }

    override suspend fun updateUser(username: String, password: String, user: User) {
        userRepository.updateUser(username, password, user)
    }
}