package com.example.skripsipresensiupnvj.di

import com.example.skripsipresensiupnvj.feature_presensi.data.data_source.FirestoreDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FirestoreModule {
    @Provides
    fun provideFirestoreDataSource(): FirestoreDataSource {
        return FirestoreDataSource()
    }
}