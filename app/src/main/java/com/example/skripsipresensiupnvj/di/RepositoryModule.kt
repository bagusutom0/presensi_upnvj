package com.example.skripsipresensiupnvj.di

import com.example.skripsipresensiupnvj.feature_presensi.data.repository.KegiatanRepositoryImpl
import com.example.skripsipresensiupnvj.feature_presensi.data.repository.KehadiranRepositoryImpl
import com.example.skripsipresensiupnvj.feature_presensi.data.repository.UserRepositoryImpl
import com.example.skripsipresensiupnvj.feature_presensi.domain.repository.KegiatanRepository
import com.example.skripsipresensiupnvj.feature_presensi.domain.repository.KehadiranRepository
import com.example.skripsipresensiupnvj.feature_presensi.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun provideKegiatanRepository(kegiatanRepositoryImpl: KegiatanRepositoryImpl): KegiatanRepository

    @Binds
    abstract fun provideKehadiranRepository(kehadiranRepositoryImpl: KehadiranRepositoryImpl): KehadiranRepository
}