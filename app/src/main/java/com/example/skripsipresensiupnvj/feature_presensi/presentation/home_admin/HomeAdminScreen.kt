package com.example.skripsipresensiupnvj.feature_presensi.presentation.home_admin

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kegiatan
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.Screen
import com.example.skripsipresensiupnvj.ui.theme.Purple40
import com.example.skripsipresensiupnvj.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun HomeAdminScreen(
    navController: NavController,
    viewModel: HomeAdminViewModel = hiltViewModel()
) {
    val listKegiatan = viewModel.state.observeAsState()
    val loading = viewModel.loading.observeAsState()
    val errorMessage = viewModel.errorMessage.observeAsState()

    BackHandler {
        navController.navigate(Screen.LoginScreen.route)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getListKegiatan()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) Purple80 else Purple40,
                    titleContentColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                ),
                title = { Text(text = "Administrasi Presensi") },
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(58.dp))
            Button(
                onClick = { navController.navigate(Screen.AddKegiatanScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Tambah Kegiatan")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate(Screen.ConfirmPesertaScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Konfirmasi Peserta")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Kegiatan Berlangsung",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (listKegiatan.value?.size == 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Tidak ada kegiatan berlangsung")
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listKegiatan.value?: listOf()) {
                    KegiatanListItem(kegiatan = it)
                }
            }
            Text(text = errorMessage.value ?: "")
        }
        if (loading.value == true) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun KegiatanListItem(kegiatan: Kegiatan) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Judul  : ${kegiatan.judul}")
            Text(text = "Waktu  : ${kegiatan.waktuMulai} - ${kegiatan.waktuSelesai}")
            Text(text = "Tanggal: ${kegiatan.tanggal}")
            Text(text = "Lokasi : ${kegiatan.lokasi}")
        }
    }
}