package  com.example.skripsipresensiupnvj.feature_presensi.data.data_store

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
class Session @Inject constructor(private val context: Context) {
    companion object {
        const val TAG = "Session"
    }
    private val USER_KEY = stringPreferencesKey("user")

    val userFlow: Flow<User?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_KEY]?.let { Json.decodeFromString<User>(it) }
        }

//    fun isUserLoggedIn(): Flow<Boolean> {
//        return dataStore.data
//            .catch {
//                emit(emptyPreferences())
//            }.map { preference ->
//                preference[userLoginState] ?: false
//                val isLoggedIn = preference[userLoginState] ?: false
//                Log.d(TAG, "isUserLoggedIn: $isLoggedIn")
//                isLoggedIn
//            }
//    }
//
//    suspend fun setUserLoggedIn(isUserLoggedIn: Boolean) {
//        dataStore.edit { preference ->
//            Log.d(TAG, "setUserLoggedIn: $isUserLoggedIn")
//            preference[userLoginState] = isUserLoggedIn
//        }
//    }
//
//    fun getUser(): Flow<User> {
//        return dataStore.data
//            .catch {
//                emit(emptyPreferences())
//            }.map { preferences ->
//                val user = User(
//                    username = preferences[usrName] ?: "",
//                    password = preferences[pass] ?: "",
//                    jabatan = preferences[jbtn] ?: "",
//                    nama = preferences[namaUser] ?: "",
//                    isConfirmed = preferences[confirmState] ?: false,
//                    deviceId = preferences[idDevice] ?: ""
//                )
//                Log.d(TAG, "getUser: $user")
//                user
//            }
//    }

    suspend fun setUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = Json.encodeToString(user)
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit {
            Log.d(TAG, "clearSession")
            it.clear()
        }
    }
}