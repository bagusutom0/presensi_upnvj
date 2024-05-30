package com.example.skripsipresensiupnvj.feature_presensi.presentation.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsipresensiupnvj.feature_presensi.data.data_store.Session
import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.User
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val session: Session,
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val _state = MutableLiveData<Resource<User>>()
    val state: LiveData<Resource<User>> = _state

    fun getUser(username: String, password: String) {
        viewModelScope.launch {
            userUseCase.getUser(username, password)
                .catch {
                    _state.postValue(Resource.Error(it.message.toString()))
                }
                .collect {
                    _state.postValue(it)
                }
        }
    }

    fun deleteLoginSession() {
        viewModelScope.launch(Dispatchers.Main) {
            session.setUserLoggedIn(false)
            session.setUser(User())
            session.clearSession()

            session.isUserLoggedIn()
                .collect {
                    Log.d("DeleteLoginSession", "deleteLoginSession: userLoginState = $it")
                }
        }
    }

    fun updateUser(
        username: String,
        password: String,
        namaEdit: String,
        jabatanEdit: String,
        usernameEdit: String,
        passwordEdit: String
    ) {
        viewModelScope.launch {
            userUseCase.updateUser(
                username = username,
                password = password,
                user = User(
                    nama = namaEdit,
                    jabatan = jabatanEdit,
                    username = usernameEdit,
                    password = passwordEdit
                )
            )
        }
    }
}