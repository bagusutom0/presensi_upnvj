package com.example.skripsipresensiupnvj.feature_presensi.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.User
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    fun inputUser(
        username: String,
        password: String,
        nama: String,
        jabatan: String,
        deviceId: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            userUseCase.inputUser(
                User(
                    username,
                    password,
                    jabatan,
                    nama,
                    isConfirmed = false,
                    deviceId
                )
            )
        }
    }
}