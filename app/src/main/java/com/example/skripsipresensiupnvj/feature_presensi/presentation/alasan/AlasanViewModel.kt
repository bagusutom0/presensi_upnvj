package com.example.skripsipresensiupnvj.feature_presensi.presentation.alasan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kehadiran.KehadiranUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlasanViewModel @Inject constructor(
    private val kehadiranUseCase: KehadiranUseCase,
): ViewModel() {
    fun submitAlasanKehadiran(idKegiatan: String, username: String, password: String, alasan: String) {
        viewModelScope.launch {
            kehadiranUseCase.submitAlasanKehadiran(idKegiatan,username, password, alasan)
        }
    }
}