package com.example.skripsipresensiupnvj.feature_presensi.presentation.home_user

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.Kegiatan
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.Screen
import com.example.skripsipresensiupnvj.ui.theme.Purple40
import com.example.skripsipresensiupnvj.ui.theme.Purple80
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun HomeUserScreen(
    navController: NavController,
    username: String,
    password: String,
    viewModel: HomeUserViewModel = hiltViewModel()
) {
    var canExit by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current

    val listKegiatan = viewModel.state.observeAsState()
    val loading = viewModel.loading.observeAsState()
    val errorMessage = viewModel.errorMessage.observeAsState()

    LaunchedEffect(key1 = canExit) {
        if (canExit) {
            delay(2000)
            canExit = false
        }

        viewModel.getListKegiatan()
    }

    BackHandler(enabled = true) {
        if (canExit) {
            viewModel.isUserLogin.observe(owner) {
                if (it) {
                    context.startActivity(Intent(Intent.ACTION_MAIN).apply {
                        addCategory(Intent.CATEGORY_HOME)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                } else {
                    navController.navigate(Screen.LoginScreen.route)
                }
            }
        } else {
            canExit = true
            Toast.makeText(context, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) Purple80 else Purple40,
                    titleContentColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                ),
                title = { Text(text = "Daftar Kegiatan") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(
                            Screen.ProfileScreen.route +
                                    "?username=${username}&password=${password}"
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "profile",
                            tint = if (isSystemInDarkTheme()) Color.Black else Color.White
                        )
                    }
                },
            )
        },
    ) {
        Column {
            Spacer(modifier = Modifier.height(68.dp))
            if (listKegiatan.value?.size == 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Tidak ada kegiatan berlangsung")
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listKegiatan.value ?: listOf()) {
                    KegiatanListItem(
                        kegiatan = it,
                    )
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
fun KegiatanListItem(
    kegiatan: Kegiatan,
) {
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
            .clickable {
                Toast
                    .makeText(context, "Mohon scan tag NFC", Toast.LENGTH_SHORT)
                    .show()
            }
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