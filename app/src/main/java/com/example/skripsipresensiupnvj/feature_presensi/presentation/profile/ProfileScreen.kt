package com.example.skripsipresensiupnvj.feature_presensi.presentation.profile

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.Screen
import com.example.skripsipresensiupnvj.ui.theme.Purple40
import com.example.skripsipresensiupnvj.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavController,
    username: String,
    password: String,
    owner: LifecycleOwner,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var namaEdit by remember { mutableStateOf("") }
    var jabatanEdit by remember { mutableStateOf("") }
    var usernameEdit by remember { mutableStateOf("") }
    var passwordEdit by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getUser(username = username, password = password)
        viewModel.state.observe(owner) {
            when (it) {
                is Resource.Success -> {
                    namaEdit = it.data?.nama ?: ""
                    jabatanEdit = it.data?.jabatan ?: ""
                    usernameEdit = it.data?.username ?: ""
                    passwordEdit = it.data?.password ?: ""

                }

                is Resource.Loading -> {
                    Log.d("ProfileScreen", "ProfileScreen: Loading")
                }

                is Resource.Error -> {
                    val message = it.message ?: "Terjadi kesalahan!"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    Log.e("LoginScreen", "Error: ${it.message}")
                }
            }
        }
    }

    BackHandler {
        navController.navigate(
            Screen.HomeUserScreen.route +
                    "?username=$usernameEdit&password=$passwordEdit"
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) Purple80 else Purple40,
                    titleContentColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                ),
                title = { Text(text = "Data Profil") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(
                                Screen.HomeUserScreen.route +
                                        "?username=$usernameEdit&password=$passwordEdit"
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
            OutlinedTextField(
                value = namaEdit,
                onValueChange = { namaEdit = it },
                label = { Text(text = "Nama") },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = jabatanEdit,
                onValueChange = { jabatanEdit = it },
                label = { Text(text = "Jabatan") },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = usernameEdit,
                onValueChange = { usernameEdit = it },
                label = { Text(text = "Username") },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = passwordEdit,
                onValueChange = { passwordEdit = it },
                label = { Text("Password") },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )
            Spacer(modifier = Modifier.height(42.dp))
            Button(
                onClick = {
                    if (
                        namaEdit.isNotBlank() &&
                        jabatanEdit.isNotBlank() &&
                        usernameEdit.isNotBlank() &&
                        passwordEdit.isNotBlank()
                    ) {
                        viewModel.updateUser(
                            username,
                            password,
                            namaEdit,
                            jabatanEdit,
                            usernameEdit,
                            passwordEdit
                        )

                        navController.navigate(
                            Screen.HomeUserScreen.route +
                                    "?username=$usernameEdit&password=$passwordEdit"
                        )
                    } else {
                        Toast.makeText(context, "Mohon isi semua kolom", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Simpan")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.deleteLoginSession(navController)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Keluar")
            }
        }
    }
}