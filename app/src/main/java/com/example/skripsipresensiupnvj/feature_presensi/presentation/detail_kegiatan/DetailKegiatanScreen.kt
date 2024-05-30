package com.example.skripsipresensiupnvj.feature_presensi.presentation.detail_kegiatan

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ComponentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.presentation.add_kegiatan.getLastUserLocation
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.NfcHandler
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.Screen
import com.example.skripsipresensiupnvj.ui.theme.Purple40
import com.example.skripsipresensiupnvj.ui.theme.Purple80
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKegiatanScreen(
    navController: NavController,
    id: String,
    owner: LifecycleOwner,
    viewModel: DetailKegiatanViewModel = hiltViewModel(),
    username: String,
    password: String,
    handler: NfcHandler
) {
    val context = LocalContext.current as ComponentActivity

    var detail by remember { mutableStateOf("") }
    var waktuMulai by remember { mutableStateOf("") }
    var waktuSelesai by remember { mutableStateOf("") }
    var presensiMasuk by remember { mutableStateOf("Tidak hadir") }
    var presensiKeluar by remember { mutableStateOf("Tidak hadir") }
    var tanggal by remember { mutableStateOf("") }
    var judul by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var arrayMinCoor by remember { mutableStateOf(listOf<String>()) }
    var arrayMaxCoor by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getKegiatanById(id)

        viewModel.kegiatanState.observe(owner) {
            when (it) {
                is Resource.Success -> {
                    detail = it.data?.detail ?: ""
                    waktuMulai = it.data?.waktuMulai ?: ""
                    waktuSelesai = it.data?.waktuSelesai ?: ""
                    tanggal = it.data?.tanggal ?: ""
                    judul = it.data?.judul ?: ""
                    lokasi = it.data?.lokasi ?: ""
                    val minCoordinate = it.data?.minCoordinate ?: ""
                    val maxCoordinate = it.data?.maxCoordinate ?: ""

                    arrayMinCoor = toArray(minCoordinate)
                    arrayMaxCoor = toArray(maxCoordinate)

                    viewModel.getKehadiran(
                        username = username,
                        password = password,
                        judul = judul,
                        lokasi = lokasi
                    )
                }

                is Resource.Loading -> {
                    Log.d("DetailKegiatanScreen", "DetailKegiatanScreen: Loading")
                }

                is Resource.Error -> {
                    Log.e("DetailKegiatanScreen", "DetailKegiatanScreen: ${it.message}")
                }
            }
        }

        viewModel.kehadiranState.observe(owner) {
            when (it) {
                is Resource.Success -> {
                    presensiMasuk = it.data?.presensiMasuk ?: "Tidak hadir"
                    presensiKeluar = it.data?.presensiKeluar ?: "Tidak hadir"
                }

                is Resource.Loading -> {
                    Log.d("DetailKegiatanScreen", "DetailKegiatanScreen: Loading")
                }

                is Resource.Error -> {
                    Log.e("DetailKegiatanScreen", "DetailKegiatanScreen: ${it.message}")
                }
            }
        }
    }

    BackHandler {
        navController.navigate(
            Screen.HomeUserScreen.route +
                    "?username=$username&password=$password"
        )
    }

    DisposableEffect(Unit) {
        handler.initialize()
        onDispose { handler.cleanup() }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) Purple80 else Purple40,
                    titleContentColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                ),
                title = { Text(text = "Detail Kegiatan") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(
                                Screen.HomeUserScreen.route +
                                        "?username=$username&password=$password"
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back to Daftar Kegiatan",
                            tint = Color.White
                        )
                    }
                },
            )
        },

        ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(68.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Judul  : $judul")
                    Text(text = "Detail : $detail")
                    Text(text = "Waktu  : $waktuMulai - $waktuSelesai")
                    Text(text = "Tanggal: $tanggal")
                    Text(text = "Lokasi : $lokasi")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Status Kehadiran",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Presensi Masuk : $presensiMasuk")
                    Text(text = "Presensi Keluar : $presensiKeluar")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (presensiMasuk != "Hadir") {
                        getLastUserLocation(
                            context,
                            onGetLastLocationSuccess = {
                                if (
                                    it.first >= arrayMinCoor[0].toDouble() &&
                                    it.first <= arrayMaxCoor[0].toDouble() &&
                                    it.second >= arrayMinCoor[1].toDouble() &&
                                    it.second <= arrayMaxCoor[1].toDouble()
                                ) {
                                    val date = LocalDate.now()
                                    val formatter =
                                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                    val formatedDate = date.format(formatter)

                                    if (tanggal == formatedDate) {
                                        val currentTime =
                                            LocalDateTime.now()
                                        val hour =
                                            if (waktuMulai.length == 4) {
                                                "${waktuMulai[0]}".toInt()
                                            } else {
                                                "${waktuMulai[0]}${waktuMulai[1]}".toInt()
                                            }

                                        if (currentTime.hour <= hour + 1) {
                                            viewModel.presensiMasuk(
                                                username,
                                                password,
                                                judul,
                                                lokasi
                                            )
                                            viewModel.getKehadiran(
                                                username,
                                                password,
                                                judul,
                                                lokasi
                                            )
                                            Toast.makeText(
                                                context,
                                                "Presensi Masuk Berhasil",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Anda tidak berada di waktu presensi!",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Anda tidak berada di tanggal presensi!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Anda berada diluar lokasi!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            },
                            onGetLastLocationFailed = {
                                Toast.makeText(
                                    context,
                                    "Gagal memeriksa lokasi!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Anda sudah melakukan presensi Masuk!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Presensi Masuk")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (presensiMasuk == "Tidak hadir") {
                        Toast.makeText(
                            context,
                            "Mohon melakukan presensi masuk terlebih dahulu!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (presensiKeluar == "Hadir") {
                            Toast.makeText(
                                context,
                                "Anda telah melakukan presensi keluar!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            getLastUserLocation(
                                context,
                                onGetLastLocationSuccess = {
                                    if (
                                        it.first >= arrayMinCoor[0].toDouble() &&
                                        it.first <= arrayMaxCoor[0].toDouble() &&
                                        it.second >= arrayMinCoor[1].toDouble() &&
                                        it.second <= arrayMaxCoor[1].toDouble()
                                    ) {
                                        val date = LocalDate.now()
                                        val formatter =
                                            DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                        val formatedDate =
                                            date.format(formatter)

                                        if (tanggal == formatedDate) {
                                            val currentTime =
                                                LocalDateTime.now()
                                            val hour =
                                                if (waktuMulai.length == 4) {
                                                    "${waktuMulai[0]}".toInt()
                                                } else {
                                                    "${waktuMulai[0]}${waktuMulai[1]}".toInt()
                                                }

                                            if (currentTime.hour <= hour + 1) {
                                                viewModel.presensiKeluar(
                                                    username,
                                                    password,
                                                    judul,
                                                    lokasi
                                                )
                                                viewModel.getKehadiran(
                                                    username,
                                                    password,
                                                    judul,
                                                    lokasi
                                                )
                                                Toast.makeText(
                                                    context,
                                                    "Presensi Keluar Berhasil",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Anda tidak berada di waktu presensi!",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Anda tidak berada di tanggal presensi!",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Anda berada diluar lokasi!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                },
                                onGetLastLocationFailed = {
                                    Toast.makeText(
                                        context,
                                        "Gagal memeriksa lokasi!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Presensi Keluar")
            }

            Button(
                onClick = { navController.navigate(Screen.AlasanScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Izin/Telat")
            }
        }
    }
}

fun toArray(value: String): List<String> {
    val temp = value.substring(1, value.length - 1)
    return temp.split(":")
}