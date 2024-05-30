package com.example.skripsipresensiupnvj.feature_presensi.presentation.add_kegiatan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kegiatan
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kegiatan.KegiatanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddKegiatanViewModel @Inject constructor(
    private val kegiatanUseCase: KegiatanUseCase
): ViewModel() {
    private val _nfcMessage = MutableLiveData<Resource<String>>()
    val nfcMessage: LiveData<Resource<String>> = _nfcMessage

    fun inputKegiatan(kegiatan: Kegiatan) {
        viewModelScope.launch(Dispatchers.IO) {
            kegiatanUseCase.inputKegiatan(kegiatan)
        }
    }

    fun getIdKegiatan(judul: String, lokasi: String) {
        viewModelScope.launch {
            kegiatanUseCase.getIdKegiatan(judul, lokasi)
                .catch {
                    _nfcMessage.postValue(Resource.Error(it.message.toString()))
                }
                .collect {
                    _nfcMessage.postValue(it)
                }
        }
    }
}