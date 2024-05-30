package com.example.skripsipresensiupnvj.feature_presensi.presentation.confirm_peserta

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.skripsipresensiupnvj.feature_presensi.domain.model.User
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.Screen
import com.example.skripsipresensiupnvj.ui.theme.Purple40
import com.example.skripsipresensiupnvj.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ConfirmPesertaScreen(
    navController: NavController,
    viewModel: ConfirmPesertaViewModel = hiltViewModel()
) {
    val unConfirmedUsers = viewModel.state.observeAsState()
    val loading = viewModel.loading.observeAsState()
    val errorMessage = viewModel.errorMessage.observeAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getUnconfirmedUser()
    }

    BackHandler {
        navController.navigate(Screen.HomeAdminScreen.route)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) Purple80 else Purple40,
                    titleContentColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                ),
                title = { Text(text = "Konfirmasi Peserta") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(Screen.HomeAdminScreen.route) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back to Home Admin",
                            tint = Color.White
                        )
                    }
                },
            )
        },
    ) {
        Column {
            Spacer(modifier = Modifier.height(58.dp))
            if (unConfirmedUsers.value?.size == 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Semua peserta telah dikonfirmasi!")
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(unConfirmedUsers.value?: listOf()) {
                    UserListItem(user = it)
                }
            }
            Text(text = errorMessage.value?: "")
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
fun UserListItem(user: User) {
    var showConfirmPeserta by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
            .clickable { showConfirmPeserta = true }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Nama    : ${user.nama}")
            Text(text = "Jabatan : ${user.jabatan}")
        }
    }

    if (showConfirmPeserta) {
        ConfirmPesertaDialog(
            onDismiss = { showConfirmPeserta = false },
            user = user
        )
    }
}
