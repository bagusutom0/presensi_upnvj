package com.example.skripsipresensiupnvj.feature_presensi.presentation.home_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsipresensiupnvj.feature_presensi.data.data_store.Session
import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kegiatan
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kegiatan.KegiatanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeUserViewModel @Inject constructor(
    private val session: Session,
    private val kegiatanUseCase: KegiatanUseCase,
): ViewModel() {
    private val _state = MutableLiveData<List<Kegiatan>>()
    val state: LiveData<List<Kegiatan>> = _state

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

//    fun getIdKegiatan(judul: String, lokasi: String): String {
//        var idKegiatan = ""
//        viewModelScope.launch {
//            kegiatanUseCase.getIdKegiatan(judul, lokasi)
//                .collect {
//                    idKegiatan = when (it) {
//                        is Resource.Success -> {
//                            it.data.toString()
//                        }
//
//                        is Resource.Loading -> {
//                            ""
//                        }
//
//                        is Resource.Error -> {
//                            ""
//                        }
//                    }
//                }
//        }
//        return idKegiatan
//    }

    fun getListKegiatan() {
        viewModelScope.launch {
            kegiatanUseCase.getListKegiatan()
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
}