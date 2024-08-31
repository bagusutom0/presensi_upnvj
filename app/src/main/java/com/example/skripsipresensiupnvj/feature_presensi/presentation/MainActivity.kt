package com.example.skripsipresensiupnvj.feature_presensi.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
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
    private lateinit var pendingIntent: PendingIntent
    private lateinit var deviceId: String
    private val nfcMessage = MutableLiveData<String>()
    private lateinit var intentFiltersArray: Array<IntentFilter>
    private lateinit var techListsArray: Array<Array<String>>
    private lateinit var adapter: NfcAdapter
    private lateinit var nfcHandler: NfcHandler
    private lateinit var navController: NavHostController

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
                    navController = rememberNavController()
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
                                nfcMessage = nfcMessage
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
                                password = password,
                                nfcMessage = nfcMessage
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
                            val id = it.arguments?.getString("idKegiatan") ?: ""
                            val username = it.arguments?.getString("username") ?: ""
                            val password = it.arguments?.getString("password") ?: ""
                            DetailKegiatanScreen(
                                navController = navController,
                                id = id,
                                username = username,
                                password = password,
                                handler = nfcHandler,
                                nfcMessage = nfcMessage,
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
                            val id = it.arguments?.getString("idKegiatan") ?: ""
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

        // NFC
        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_MUTABLE)
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            try {
                addDataType("text/plain")    /* Handles all MIME based dispatches.
                                 You should specify only the ones that you need. */
            } catch (e: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("fail", e)
            }
        }

        intentFiltersArray = arrayOf(ndef)
        techListsArray = arrayOf(arrayOf(Ndef::class.java.name))
        adapter = NfcAdapter.getDefaultAdapter(this)
        nfcHandler = NfcHandler()
    }

    public override fun onPause() {
        super.onPause()
        adapter.disableForegroundDispatch(this)
    }

    public override fun onResume() {
        super.onResume()
        adapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray)
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tagFromIntent: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        nfcHandler.handleNfcTag(tagFromIntent, this, navController)

        if (tagFromIntent != null) {
            Toast.makeText(this, "Tag NFC terdeteksi", Toast.LENGTH_SHORT).show()
            nfcMessage.postValue(nfcHandler.readNdefMessage())
        }
    }

    @SuppressLint("HardwareIds")
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
            startActivity(Intent(this, MainActivity::class.java))
            getDeviceID()
            checkNfcAvailability()
            checkLocationAvailability()
        }
    }

    @SuppressLint("HardwareIds")
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