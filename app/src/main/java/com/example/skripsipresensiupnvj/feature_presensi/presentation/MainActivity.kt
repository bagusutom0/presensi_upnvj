package com.example.skripsipresensiupnvj.feature_presensi.presentation

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.skripsipresensiupnvj.feature_presensi.presentation.add_kegiatan.AddKegiatanScreen
import com.example.skripsipresensiupnvj.feature_presensi.presentation.alasan.AlasanScreen
import com.example.skripsipresensiupnvj.feature_presensi.presentation.confirm_peserta.ConfirmPesertaScreen
import com.example.skripsipresensiupnvj.feature_presensi.presentation.detail_kegiatan.DetailKegiatanScreen
import com.example.skripsipresensiupnvj.feature_presensi.presentation.home_admin.HomeAdminScreen
import com.example.skripsipresensiupnvj.feature_presensi.presentation.home_user.HomeUserScreen
import com.example.skripsipresensiupnvj.feature_presensi.presentation.login.LoginScreen
import com.example.skripsipresensiupnvj.feature_presensi.presentation.profile.ProfileScreen
import com.example.skripsipresensiupnvj.feature_presensi.presentation.register.RegisterScreen
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.NfcHandler
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.Screen
import com.example.skripsipresensiupnvj.ui.theme.SkripsiPresensiUPNVJTheme
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private lateinit var nfcHandler: NfcHandler
    private lateinit var deviceId: String
    private val nfcMessageState = mutableStateOf<String?>(null)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.NFC] == true -> {
                    // Izin NFC diberikan
                    checkNfcAvailability()
                }

                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {}
                else -> {
                    // Salah satu atau lebih izin ditolak
                    Toast.makeText(this, "Izin aplikasi ditolak!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkripsiPresensiUPNVJTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.LoginScreen.route
                    ) {
                        // login
                        composable(route = Screen.LoginScreen.route) {
                            LoginScreen(
                                owner = this@MainActivity,
                                navController = navController,
                                deviceId = deviceId,
                                nfcMessageState = nfcMessageState
                            )
                        }
                        // Home User
                        composable(
                            route = Screen.HomeUserScreen.route +
                                    "?username={username}&password={password}",
                            arguments = listOf(
                                navArgument(
                                    name = "username"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument(
                                    name = "password"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                            )
                        ) {
                            val username = it.arguments?.getString("username") ?: ""
                            val password = it.arguments?.getString("password") ?: ""
                            HomeUserScreen(
                                navController = navController,
                                username = username,
                                password = password
                            )
                        }
                        // Home Admin
                        composable(route = Screen.HomeAdminScreen.route) {
                            HomeAdminScreen(navController = navController)
                        }
                        // Profile
                        composable(
                            route = Screen.ProfileScreen.route +
                                    "?username={username}&password={password}",
                            arguments = listOf(
                                navArgument(
                                    name = "username"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument(
                                    name = "password"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                            )
                        ) {
                            val username = it.arguments?.getString("username") ?: ""
                            val password = it.arguments?.getString("password") ?: ""
                            ProfileScreen(
                                navController = navController,
                                username = username,
                                password = password,
                                owner = this@MainActivity
                            )
                        }
                        // Add Kegiatan
                        composable(route = Screen.AddKegiatanScreen.route) {
                            AddKegiatanScreen(
                                navController = navController,
                                owner = this@MainActivity,
                                handler = nfcHandler,
                            )
                        }
                        // Detail Kegiatan
                        composable(
                            route = Screen.DetailKegiatanScreen.route +
                                    "?idKegiatan={idKegiatan}" +
                                    "&username={username}&password={password}",
                            arguments = listOf(
                                navArgument(
                                    name = "idKegiatan"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument(
                                    name = "username"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument(
                                    name = "password"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                            )
                        ) {
                            val id = it.arguments?.getString("id") ?: ""
                            val username = it.arguments?.getString("username") ?: ""
                            val password = it.arguments?.getString("password") ?: ""
                            DetailKegiatanScreen(
                                navController = navController,
                                id = id,
                                username = username,
                                password = password,
                                handler = nfcHandler,
                                owner = this@MainActivity
                            )
                        }
                        // Alasan
                        composable(
                            route = Screen.AlasanScreen.route +
                                    "?idKegiatan={idKegiatan}" +
                                    "&username={username}&password={password}",
                            arguments = listOf(
                                navArgument(
                                    name = "idKegiatan"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument(
                                    name = "username"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                                navArgument(
                                    name = "password"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                            )
                        ) {
                            val id = it.arguments?.getString("id") ?: ""
                            val username = it.arguments?.getString("username") ?: ""
                            val password = it.arguments?.getString("password") ?: ""
                            AlasanScreen(
                                navController = navController,
                                idKegiatan = id,
                                username = username,
                                password = password,
                            )
                        }
                        // Register
                        composable(route = Screen.RegisterScreen.route) {
                            RegisterScreen(deviceId = deviceId, navController = navController)
                        }
                        // Konfirmasi Peserta
                        composable(route = Screen.ConfirmPesertaScreen.route) {
                            ConfirmPesertaScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }

        // cek izin aplikasi
        requestNecessaryPermissions()

        // Initialize NFC adapter and pending intent
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )

        // Initialize the NfcHandler
        nfcHandler = NfcHandler()
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) as Tag?
        nfcHandler.handleNfcTag(tag)
        val ndefMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        if (ndefMessages != null) {
            val ndefMessage = ndefMessages[0] as NdefMessage
            val records = ndefMessage.records
            for (record in records) {
                if (record.tnf == NdefRecord.TNF_EXTERNAL_TYPE) {
                    val payload = String(record.payload)
                    Log.d("NFC", "Payload: $payload")
                    nfcMessageState.value = payload
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestNecessaryPermissions() {
        val permissions = mutableListOf<String>()
        if (checkSelfPermission(Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.NFC)
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissions.isNotEmpty()) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        } else {
            getDeviceID()
            checkNfcAvailability()
            checkLocationAvailability()
        }
    }

    private fun getDeviceID() {
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        deviceId = androidId
    }

    private fun checkNfcAvailability() {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC tidak tersedia pada perangkat ini!", Toast.LENGTH_SHORT)
                .show()
        } else if (!nfcAdapter.isEnabled) {
            Toast.makeText(this, "Mohon aktifkan NFC!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLocationAvailability() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Lokasi tidak diaktifkan
            Toast.makeText(this, "Mohon aktifkan lokasi!", Toast.LENGTH_SHORT).show()
        }
    }
}