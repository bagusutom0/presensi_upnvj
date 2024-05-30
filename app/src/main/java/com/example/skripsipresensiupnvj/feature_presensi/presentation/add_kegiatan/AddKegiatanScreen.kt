package com.example.skripsipresensiupnvj.feature_presensi.presentation.add_kegiatan

import android.annotation.SuppressLint
import android.nfc.NfcAdapter
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ComponentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kegiatan
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.NfcHandler
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.NfcWriteListener
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.Screen
import com.example.skripsipresensiupnvj.ui.theme.Purple40
import com.example.skripsipresensiupnvj.ui.theme.Purple80

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi")
@Composable
fun AddKegiatanScreen(
    navController: NavController,
    owner: LifecycleOwner,
    handler: NfcHandler,
    viewModel: AddKegiatanViewModel = hiltViewModel(),
) {
    var judul by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }
    var waktuMulai by remember { mutableStateOf("") }
    var waktuSelesai by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var showWaktuMulaiDialog by remember { mutableStateOf(false) }
    var showWaktuSelesaiDialog by remember { mutableStateOf(false) }
    var showTanggalDialog by remember { mutableStateOf(false) }
    var isPublic by remember { mutableStateOf(true) }
    val context = LocalContext.current as ComponentActivity
    val nfcAdapter = NfcAdapter.getDefaultAdapter(context)

    BackHandler {
        navController.navigate(Screen.HomeAdminScreen.route)
    }

    if (nfcAdapter == null) {
        Toast.makeText(context, "Perangkat anda tidak memiliki NFC", Toast.LENGTH_SHORT).show()
        return
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
                title = { Text(text = "Tambah Kegiatan") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(Screen.HomeAdminScreen.route) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back to Home Admin",
                            tint = if (isSystemInDarkTheme()) Color.Black else Color.White,
                        )
                    }
                },
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(58.dp))
            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                label = { Text(text = "Judul Kegiatan") },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = detail,
                onValueChange = { detail = it },
                label = { Text(text = "Detail Kegiatan") },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = waktuMulai,
                onValueChange = { waktuMulai = it },
                label = { Text(text = "Waktu Mulai") },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(),
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    showWaktuMulaiDialog = true
                                }
                            }
                        }
                    }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = waktuSelesai,
                onValueChange = { waktuSelesai = it },
                label = { Text(text = "Waktu Selesai") },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(),
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    showWaktuSelesaiDialog = true
                                }
                            }
                        }
                    }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = tanggal,
                onValueChange = { tanggal = it },
                label = { Text(text = "Tanggal") },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(),
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    showTanggalDialog = true
                                }
                            }
                        }
                    }
            )
            OutlinedTextField(
                value = lokasi,
                onValueChange = { lokasi = it },
                label = { Text(text = "Lokasi") },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Publik")
                Checkbox(checked = isPublic , onCheckedChange = { isPublic = it })
                Text(text = "Privat")
                Checkbox(checked = !isPublic , onCheckedChange = { isPublic = !it })
            }
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = {
                    if (
                        judul.isNotBlank() &&
                        detail.isNotBlank() &&
                        waktuMulai.isNotBlank() &&
                        waktuSelesai.isNotBlank() &&
                        lokasi.isNotBlank() &&
                        tanggal.isNotBlank()
                    ) {
                        getLastUserLocation(
                            context,
                            onGetLastLocationSuccess = {
//                                Toast.makeText(context, "testing", Toast.LENGTH_SHORT).show()
                                // radius: +- 10 meter
                                val minCoordinate =
                                    "[${it.first - 0.0001000}:${it.second - 0.001000}]"
                                val maxCoordinate =
                                    "[${it.first + 0.0001000}:${it.second + 0.001000}]"

                                viewModel.inputKegiatan(
                                    Kegiatan(
                                        judul = judul,
                                        detail = detail,
                                        waktuMulai = waktuMulai,
                                        waktuSelesai = waktuSelesai,
                                        tanggal = tanggal,
                                        lokasi = lokasi,
                                        isPublic = isPublic,
                                        minCoordinate = minCoordinate,
                                        maxCoordinate = maxCoordinate,
                                    )
                                )

                                viewModel.getIdKegiatan(judul, lokasi)

                                viewModel.nfcMessage.observe(owner) { resource ->
                                    when (resource) {
                                        is Resource.Success -> {
                                            nfcAdapter.let { adapter ->
                                                if (adapter.isEnabled) {
                                                    handler.writeNdefMessage(
                                                        resource.data ?: "",
                                                        object : NfcWriteListener {
                                                            override fun onNfcWriteSuccess() {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Berhasil menulis ke NFC",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                navController.navigate(Screen.HomeAdminScreen.route)
                                                            }

                                                            override fun onNfcWriteError(
                                                                errorMessage: String
                                                            ) {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Terjadi kesalahan, mohon ulangi",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }

                                                        })
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Tolong nyalakan fitur NFC",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }

                                        is Resource.Loading -> {
                                            Log.d(
                                                "AddKegiatanScreen",
                                                "AddKegiatanScreen: Loading"
                                            )
                                        }

                                        is Resource.Error -> {
                                            Toast.makeText(
                                                context,
                                                "Terjadi kesalahan ketika memasukan kegiatan!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            },
                            onGetLastLocationFailed = {
                                val message = it.localizedMessage ?: "Get Location Error!"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        Toast.makeText(context, "Mohon isi semua kolom!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Simpan Kegiatan")
            }
        }
        if (showWaktuMulaiDialog) {
            MyTimePickerDialog(
                onTimeSelected = { waktuMulai = it },
                onCancel = { showWaktuMulaiDialog = false }
            )
        }
        if (showWaktuSelesaiDialog) {
            MyTimePickerDialog(
                onTimeSelected = { waktuSelesai = it },
                onCancel = { showWaktuSelesaiDialog = false }
            )
        }
        if (showTanggalDialog) {
            MyDatePickerDialog(
                onDismiss = { showTanggalDialog = false },
                onDateSelected = { tanggal = it }
            )
        }
    }
}