package com.example.skripsipresensiupnvj.feature_presensi.presentation.confirm_peserta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.User
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmPesertaViewModel @Inject constructor(
    private val userUseCase: UserUseCase
):ViewModel() {
    private val _state = MutableLiveData<List<User>>()
    val state: LiveData<List<User>> = _state

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        getUnconfirmedUser()
    }

    fun getUnconfirmedUser() {
        viewModelScope.launch {
            userUseCase.getUnconfirmedUser()
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            _loading.postValue(false)
                            _state.postValue(it.data?: listOf())
                        }
                        is Resource.Loading -> {
                            _loading.postValue(true)
                        }
                        is Resource.Error -> {
                            _loading.postValue(false)
                            _errorMessage.postValue(it.message?: "Terjadi kesalahan!")
                        }
                    }
                }
        }
    }

    fun confirmUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userUseCase.confirmUser(user)
            getUnconfirmedUser()
        }
    }
}