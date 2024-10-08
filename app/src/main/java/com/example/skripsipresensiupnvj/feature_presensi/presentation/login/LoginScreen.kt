package com.example.skripsipresensiupnvj.feature_presensi.presentation.login

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.skripsipresensiupnvj.R
import com.example.skripsipresensiupnvj.feature_presensi.data.repository.Resource
import com.example.skripsipresensiupnvj.feature_presensi.presentation.util.Screen
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun LoginScreen(
    owner: LifecycleOwner,
    navController: NavController,
    deviceId: String,
    nfcMessage: MutableLiveData<String>,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var canExit by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var inputUsername by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = null) {
        Log.d("LoginScreen", "LoginScreen: launchedEffect")
        viewModel.state.observe(owner) { it ->
            when (it) {
                is Resource.Success -> {
                    loading = false
                    inputUsername = it.data?.username ?: ""
                    inputPassword = it.data?.password ?: ""
                    if (inputUsername != "" && inputPassword != "") {
                        Log.d("LoginScreen", "username & password != '' ")
                        nfcMessage.observe(owner) {
                            if (it != "") {
//                                navController.navigate(
//                                    Screen.DetailKegiatanScreen.route +
//                                            "?idKegiatan=${it}" +
//                                            "&username=${inputUsername}&password=${inputPassword}"
//                                )
                            } else {
                                navController.navigate(
                                    Screen.HomeUserScreen.route +
                                            "?username=${inputUsername}&password=${inputPassword}"
                                )
                            }
                        }
                    }
                }

                is Resource.Loading -> {
                    loading = true
                }

                is Resource.Error -> {
                    loading = false
//                            val message = "Pengguna ini tidak tersedia!"
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    Log.e("LoginScreen", "Error: ${it.message}")
                }
            }
        }

        if (canExit) {
            delay(2000)
            canExit = false
        }
    }

    BackHandler(enabled = true) {
        if (canExit) {
            context.startActivity(Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } else {
            canExit = true
            Toast.makeText(context, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_upnvj),
                contentDescription = "logo upnvj",
                modifier = Modifier.width(160.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Presensi UPNVJ", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(120.dp))
            OutlinedTextField(
                value = inputUsername,
                onValueChange = { inputUsername = it },
                label = { Text(text = "Username") },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = inputPassword,
                onValueChange = { inputPassword = it },
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
            Spacer(modifier = Modifier.height(60.dp))
            Button(
                onClick = {
                    if (inputUsername.isNotBlank() && inputPassword.isNotBlank()) {
                        if (inputUsername != "admin" && inputPassword != "admin") {
                            viewModel.getConfirmedUser(inputUsername, inputPassword, deviceId)
                            viewModel.state.observe(owner) {
                                when (it) {
                                    is Resource.Success -> {
                                        loading = false
                                        navController.navigate(
                                            Screen.HomeUserScreen.route +
                                                    "?username=${inputUsername}&password=${inputPassword}"
                                        )
                                        Toast.makeText(
                                            context,
                                            "Selamat Datang",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    is Resource.Loading -> {
                                        loading = true
                                    }

                                    is Resource.Error -> {
                                        loading = false
                                        val message = "Pengguna ini tidak tersedia!"
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    }

                                    else -> {
                                        Toast.makeText(
                                            context,
                                            "Terjadi kesalahan",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        } else {
                            navController.navigate(Screen.HomeAdminScreen.route)
                            Toast.makeText(context, "Selamat Datang", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Mohon isi semua kolom!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.RegisterScreen.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Register")
            }

        }
        if (loading) {
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