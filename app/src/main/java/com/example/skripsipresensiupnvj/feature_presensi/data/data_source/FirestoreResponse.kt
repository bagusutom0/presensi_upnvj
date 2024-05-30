package com.example.skripsipresensiupnvj.feature_presensi.data.data_source

sealed class FirestoreResponse<out R> {
    data class Success<out T>(val data: T): FirestoreResponse<T>()
    data class Error(val errorMessage: String): FirestoreResponse<Nothing>()
    object Empty: FirestoreResponse<Nothing>()
}