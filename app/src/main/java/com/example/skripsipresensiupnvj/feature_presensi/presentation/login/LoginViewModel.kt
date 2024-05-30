package com.example.skripsipresensiupnvj.feature_presensi.presentation.login

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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val session: Session
) : ViewModel() {

    private val _state = MutableLiveData<Resource<User>>()
    val state: LiveData<Resource<User>> = _state

    private val _isUserLogin = MutableLiveData(false)
    val isUserLogin: LiveData<Boolean> = _isUserLogin

    fun getUserLoginState() {
        viewModelScope.launch {
            session.isUserLoggedIn().collect {
                Log.d("LoginViewModel", "isUserLoggedIn: $it")
                _isUserLogin.postValue(it)
            }

            if (_isUserLogin.value != null && _isUserLogin.value != false) {
                session.getUser().collect {
                    _state.postValue(Resource.Success(it))
                }
            }
        }
    }

    fun getConfirmedUser(username: String, password: String, deviceId: String) {
        viewModelScope.launch {
            userUseCase.getConfirmedUser(username, password, deviceId)
                .catch {
                    Log.e("LoginViewModel", "getConfirmedUser Error: ${it.message}")
                    _state.postValue(Resource.Error(it.message.toString()))
                }
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            session.setUserLoggedIn(true)
                            session.setUser(it.data!!)
                            _isUserLogin.postValue(true)
                            _state.postValue(it)
                            Log.d("LoginViewModel", "User logged in successfully")
                        }

                        else -> {

                        }
                    }
                }
        }
    }
}