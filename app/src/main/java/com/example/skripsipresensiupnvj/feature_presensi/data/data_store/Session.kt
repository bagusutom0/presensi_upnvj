package  com.example.skripsipresensiupnvj.feature_presensi.data.data_store

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class Session @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val TAG = "Session"
        const val DATA = "Data"
        private const val UserLoginState = "UserLoginState"
        private const val Username = "Username"
        private const val Password = "pass"
        private const val Jabatan = "Jbtn"
        private const val Nama = "NamaUser"
        private const val ConfirmState = "ConfirmState"
        private const val IdDevice = "IdDevice"
        val userLoginState = booleanPreferencesKey(UserLoginState)
        val usrName = stringPreferencesKey(Username)
        val pass = stringPreferencesKey(Password)
        val jbtn = stringPreferencesKey(Jabatan)
        val namaUser = stringPreferencesKey(Nama)
        val confirmState = booleanPreferencesKey(ConfirmState)
        val idDevice = stringPreferencesKey(IdDevice)
    }

    fun isUserLoggedIn(): Flow<Boolean> {
        return dataStore.data
            .catch {
                emit(emptyPreferences())
            }.map { preference ->
                preference[userLoginState] ?: false
                val isLoggedIn = preference[userLoginState] ?: false
                Log.d(TAG, "isUserLoggedIn: $isLoggedIn")
                isLoggedIn
            }
    }

    suspend fun setUserLoggedIn(isUserLoggedIn: Boolean) {
        dataStore.edit { preference ->
            Log.d(TAG, "setUserLoggedIn: $isUserLoggedIn")
            preference[userLoginState] = isUserLoggedIn
        }
    }

    fun getUser(): Flow<User> {
        return dataStore.data
            .catch {
                emit(emptyPreferences())
            }.map { preferences ->
                val user = User(
                    username = preferences[usrName] ?: "",
                    password = preferences[pass] ?: "",
                    jabatan = preferences[jbtn] ?: "",
                    nama = preferences[namaUser] ?: "",
                    isConfirmed = preferences[confirmState] ?: false,
                    deviceId = preferences[idDevice] ?: ""
                )
                Log.d(TAG, "getUser: $user")
                user
            }
    }

    suspend fun setUser(user: User) {
        dataStore.edit { preference ->
            preference[usrName] = user.username
            preference[pass] = user.password
            preference[jbtn] = user.jabatan
            preference[namaUser] = user.nama
            preference[confirmState] = user.isConfirmed
            preference[idDevice] = user.deviceId
        }
    }

    suspend fun clearSession() {
        dataStore.edit {
            Log.d(TAG, "clearSession")
            it.clear()
        }
    }
}