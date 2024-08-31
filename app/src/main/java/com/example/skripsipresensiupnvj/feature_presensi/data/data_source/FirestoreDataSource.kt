package com.example.skripsipresensiupnvj.feature_presensi.data.data_source

import android.util.Log
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kegiatan
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kehadiran
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirestoreDataSource {
    private val TAG = "FirestoreDataSource"
    private val db = Firebase.firestore
    private val userRef = db.collection("users")
    private val kegiatanRef = db.collection("kegiatan")
    private val kehadiranRef = db.collection("kehadiran")

    suspend fun inputUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        try {
            userRef.add(user).await()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    suspend fun updateUser(username: String, password: String, user: User) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userRef
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val updatedUser = User(
                                username = user.username,
                                password = user.password,
                                jabatan = user.jabatan,
                                nama = user.nama,
                                isConfirmed = true
                            )
                            userRef.document(document.id).set(updatedUser)
                                .addOnSuccessListener {
                                    Log.d(TAG, "updateUser: bisa cuy")
                                }
                                .addOnFailureListener {
                                    Log.e(TAG, "updateUser: $it")
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents.", exception)
                    }
                    .await()
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }

    suspend fun confirmUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        try {
            userRef
                .whereEqualTo("username", user.username)
                .whereEqualTo("password", user.password)
                .whereEqualTo("nama", user.nama)
                .whereEqualTo("jabatan", user.jabatan)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        userRef.document(document.id).update("confirmed", true)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
                .await()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    fun getUser(username: String, password: String): Flow<FirestoreResponse<User>> {
        return channelFlow {
            try {
                userRef
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            val data = document.toObject<User>()
                            trySend(FirestoreResponse.Success(data ?: User()))
                        }
                    }
                    .addOnFailureListener { exception ->
                        trySend(FirestoreResponse.Error(exception.toString()))
                        Log.e(TAG, exception.toString())
                    }
                    .await()
            } catch (e: Exception) {
                trySend(FirestoreResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }
    }

    fun getConfirmedUser(
        username: String,
        password: String,
        deviceId: String
    ): Flow<FirestoreResponse<User>> {
        return channelFlow {
            try {
                userRef
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                    .whereEqualTo("deviceId", deviceId)
                    .whereEqualTo("confirmed", true)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            val data = document.toObject<User>()
                            Log.d(TAG, "getConfirmedUser: $data")
                            trySend(FirestoreResponse.Success(data ?: User()))
                        }
                    }
                    .addOnFailureListener { exception ->
                        trySend(FirestoreResponse.Error(exception.toString()))
                        Log.e(TAG, exception.toString())
                    }
                    .await()
            } catch (e: Exception) {
                trySend(FirestoreResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }
    }

    fun getUnconfirmedUser(): Flow<FirestoreResponse<List<User>>> {
        return channelFlow {
            try {
                val users = mutableListOf<User>()
                userRef
                    .whereEqualTo("confirmed", false)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            val data = document.toObject<User>()
                            users.add(data ?: User())
                        }
                    }
                    .addOnFailureListener { exception ->
                        trySend(FirestoreResponse.Error(exception.toString()))
                        Log.e(TAG, exception.toString())
                    }
                    .await()
                trySend(FirestoreResponse.Success(users))
            } catch (e: Exception) {
                trySend(FirestoreResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }
    }

    suspend fun inputKegiatan(kegiatan: Kegiatan) = CoroutineScope(Dispatchers.IO).launch {
        try {
            kegiatanRef.add(kegiatan).await()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    fun getListKegiatan(): Flow<FirestoreResponse<List<Kegiatan>>> {
        return channelFlow {
            try {
                val listKegiatan = mutableListOf<Kegiatan>()
                kegiatanRef
                    .whereEqualTo("public", true)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            val data = document.toObject<Kegiatan>()
                            listKegiatan.add(data ?: Kegiatan())
                        }
                    }
                    .addOnFailureListener { exception ->
                        trySend(FirestoreResponse.Error(exception.toString()))
                        Log.e(TAG, exception.toString())
                    }
                    .await()
                trySend(FirestoreResponse.Success(listKegiatan))
            } catch (e: Exception) {
                trySend(FirestoreResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }
    }

    fun getKegiatan(judul: String, lokasi: String): Flow<FirestoreResponse<Kegiatan>> {
        return channelFlow {
            try {
                kegiatanRef
                    .whereEqualTo("judul", judul)
                    .whereEqualTo("lokasi", lokasi)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            val data = document.toObject<Kegiatan>()
                            Log.d(TAG, "getKegiatan: $data")
                            trySend(FirestoreResponse.Success(data ?: Kegiatan()))
                        }
                    }
                    .addOnFailureListener { exception ->
                        trySend(FirestoreResponse.Error(exception.toString()))
                        Log.e(TAG, exception.toString())
                    }
                    .await()
            } catch (e: Exception) {
                trySend(FirestoreResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }
    }

    fun getKegiatanById(id: String): Flow<FirestoreResponse<Kegiatan>> {
        return channelFlow {
            try {
                kegiatanRef
                    .document(id)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val data = document.toObject<Kegiatan>()
                            Log.d(TAG, "getKegiatanById: $data")
                            trySend(FirestoreResponse.Success(data ?: Kegiatan()))
                        } else {
                            trySend(FirestoreResponse.Error("document tidak ada"))
                            Log.e(TAG, "document tidak ada")
                        }
                    }
                    .addOnFailureListener { exception ->
                        trySend(FirestoreResponse.Error(exception.toString()))
                        Log.e(TAG, exception.toString())
                    }
                    .await()
            } catch (e: Exception) {
                trySend(FirestoreResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }
    }

    fun getIdKegiatan(judul: String, lokasi: String): Flow<FirestoreResponse<String>> {
        return channelFlow {
            try {
                kegiatanRef
                    .whereEqualTo("judul", judul)
                    .whereEqualTo("lokasi", lokasi)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            trySend(FirestoreResponse.Success(document.id))
                        }
                    }
                    .addOnFailureListener { exception ->
                        trySend(FirestoreResponse.Error(exception.toString()))
                        Log.e(TAG, exception.toString())
                    }
                    .await()
            } catch (e: Exception) {
                trySend(FirestoreResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }
    }

    fun getIdKegiatanFromCoordinate(
        minCoordinate: String,
        maxCoordinate: String
    ): Flow<FirestoreResponse<String>> {
        return channelFlow {
            Log.d(TAG, "getIdKegiatanFromCoordinate: $minCoordinate")
            Log.d(TAG, "getIdKegiatanFromCoordinate: $maxCoordinate")
            try {
                kegiatanRef
                    .whereEqualTo("minCoordinate", minCoordinate)
                    .whereEqualTo("maxCoordinate", maxCoordinate)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            trySend(FirestoreResponse.Success(document.id))
                            Log.d(TAG, "getIdKegiatanFromCoordinate: ${document.id}")
                        }
                    }
                    .addOnFailureListener { exception ->
                        trySend(FirestoreResponse.Error(exception.toString()))
                        Log.e(TAG, exception.toString())
                    }
                    .await()
            } catch (e: Exception) {
                trySend(FirestoreResponse.Error(e.toString()))
                Log.e(TAG, e.toString())
            }
        }
    }

    fun getKehadiran(
        username: String,
        password: String,
        judul: String,
        lokasi: String
    ): Flow<FirestoreResponse<Kehadiran>> {
        return channelFlow {
            try {
                var userId = ""
                var kegiatanId = ""
                userRef
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnSuccessListener {
                        for (document in it) {
                            userId = document.id
                        }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "getKehadiran: $it")
                    }
                    .await()

                kegiatanRef
                    .whereEqualTo("judul", judul)
                    .whereEqualTo("lokasi", lokasi)
                    .get()
                    .addOnSuccessListener {
                        for (document in it) {
                            kegiatanId = document.id
                        }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "getKehadiran: $it")
                    }
                    .await()

                kehadiranRef
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("kegiatanId", kegiatanId)
                    .get()
                    .addOnSuccessListener {
                        for (document in it) {
                            val data = document.toObject<Kehadiran>()
                            trySend(FirestoreResponse.Success(data))
                        }
                    }
                    .addOnFailureListener {
                        trySend(FirestoreResponse.Error(it.toString()))
                    }
                    .await()
            } catch (e: Exception) {
                trySend(FirestoreResponse.Error(e.toString()))
            }
        }
    }

    suspend fun presensiMasuk(username: String, password: String, judul: String, lokasi: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var userId = ""
            var kegiatanId = ""
            userRef
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        userId = document.id
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "presensiMasuk: $it")
                }
                .await()

            kegiatanRef
                .whereEqualTo("judul", judul)
                .whereEqualTo("lokasi", lokasi)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        kegiatanId = document.id
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "presensiMasuk: $it")
                }
                .await()

            kehadiranRef.add(
                Kehadiran(
                    userId = userId,
                    kegiatanId = kegiatanId,
                    presensiMasuk = "Hadir"
                )
            )
                .addOnSuccessListener {
                    Log.d(TAG, "presensiMasuk: Success")
                }
                .addOnFailureListener {
                    Log.e(TAG, "presensiMasuk: $it")
                }
                .await()
        }
    }

    suspend fun presensiKeluar(username: String, password: String, judul: String, lokasi: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var userId = ""
            var kegiatanId = ""
            userRef
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        userId = document.id
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "presensiKeluar: $it")
                }
                .await()

            kegiatanRef
                .whereEqualTo("judul", judul)
                .whereEqualTo("lokasi", lokasi)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        kegiatanId = document.id
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "presensiKeluar: $it")
                }
                .await()

            kehadiranRef
                .whereEqualTo("userId", userId)
                .whereEqualTo("kegiatanId", kegiatanId)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        kehadiranRef.document(document.id).update("presensiKeluar", "Hadir")
                        Log.d(TAG, "presensiKeluar: Success")
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "presensiKeluar: $it")
                }
                .await()
        }
    }

    suspend fun submitAlasanKehadiran(
        idKegiatan: String,
        username: String,
        password: String,
        alasan: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var idUser = ""

            userRef
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        idUser = document.id

                        kehadiranRef
                            .add(
                                Kehadiran(
                                    userId = idUser,
                                    kegiatanId = idKegiatan,
                                    presensiMasuk = "Izin/Telat",
                                    presensiKeluar = "Izin/Telat",
                                    alasan = alasan
                                )
                            )
                            .addOnSuccessListener {
                                Log.d(TAG, "alasanKehadiran: Success")
                            }
                            .addOnFailureListener {
                                Log.e(TAG, "alasanKehadiran: $it")
                            }
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "submitAlasanKehadiran: $it")
                }
                .await()
        }
    }
}