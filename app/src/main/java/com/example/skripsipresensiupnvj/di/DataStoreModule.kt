package com.example.skripsipresensiupnvj.di

import android.content.Context
import com.example.skripsipresensiupnvj.feature_presensi.data.data_store.Session
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): Session {
        return Session(context)
    }
}