package com.example.skripsipresensiupnvj.di

import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kegiatan.KegiatanInteractor
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kegiatan.KegiatanUseCase
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kehadiran.KehadiranInteractor
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.kehadiran.KehadiranUseCase
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.user.UserInteractor
import com.example.skripsipresensiupnvj.feature_presensi.domain.use_case.user.UserUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideUserUseCase(userInteractor: UserInteractor): UserUseCase

    @Binds
    abstract fun provideKegiatanUseCase(kegiatanInteractor: KegiatanInteractor): KegiatanUseCase

    @Binds
    abstract fun provideKehadiranUseCase(kehadiranInteractor: KehadiranInteractor): KehadiranUseCase
}