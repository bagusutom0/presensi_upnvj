package com.example.skripsipresensiupnvj.feature_presensi.presentation.detail_kegiatan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kegiatan
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kehadiran
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kegiatan.KegiatanUseCase
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kehadiran.KehadiranUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailKegiatanViewModel @Inject constructor(
    private val kegiatanUseCase: KegiatanUseCase,
    private val kehadiranUseCase: KehadiranUseCase,
): ViewModel() {
    private val _kegiatanState = MutableLiveData<Resource<Kegiatan>>()
    val kegiatanState: LiveData<Resource<Kegiatan>> = _kegiatanState

    private val _kehadiranState = MutableLiveData<Resource<Kehadiran>>()
    val kehadiranState: LiveData<Resource<Kehadiran>> = _kehadiranState

    fun getKegiatanById(id: String) {
        viewModelScope.launch {
            kegiatanUseCase.getKegiatanById(id)
        }
    }

    fun getKehadiran(username: String, password: String, judul: String, lokasi: String) {
        viewModelScope.launch {
            kehadiranUseCase.getKehadiran(username, password, judul, lokasi)
                .catch {
                    _kehadiranState.postValue(Resource.Error(it.message.toString()))
                }
                .collect {
                    _kehadiranState.postValue(it)
                }
        }
    }

    fun presensiMasuk(username: String, password: String, judul: String, lokasi: String) {
        viewModelScope.launch {
            kehadiranUseCase.presensiMasuk(username, password, judul, lokasi)
        }
    }

    fun presensiKeluar(username: String, password: String, judul: String, lokasi: String) {
        viewModelScope.launch {
            kehadiranUseCase.presensiKeluar(username, password, judul, lokasi)
        }
    }
}