package com.example.skripsipresensiupnvj.feature_presensi.presentation.alasan

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.Screen
import com.example.skripsipresensiupnvj.ui.theme.Purple40
import com.example.skripsipresensiupnvj.ui.theme.Purple80

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlasanScreen(
    navController: NavController,
    idKegiatan: String,
    username: String,
    password: String,
    viewModel: AlasanViewModel = hiltViewModel()
) {
    var alasan by remember { mutableStateOf("") }

    LaunchedEffect(key1 = null) {
//        idKegiatan = idKegiatan
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) Purple80 else Purple40,
                    titleContentColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                ),
                title = { Text(text = "Izin/Telat") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(
                                Screen.DetailKegiatanScreen.route +
                                        "?idKegiatan=${idKegiatan}" +
                                        "&username=${username}&password=${password}"
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back to Detail Kegiatan",
                            tint = Color.White
                        )
                    }
                },
            )
        },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(68.dp))
            OutlinedTextField(
                value = alasan,
                onValueChange = { alasan = it },
                label = { Text(text = "Alasan izin/telat") },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    // masukan izin pada firestore
                    viewModel.submitAlasanKehadiran(idKegiatan, username, password, alasan)

                    navController.navigate(Screen.AlasanScreen.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Kirim")
            }
        }
    }
}