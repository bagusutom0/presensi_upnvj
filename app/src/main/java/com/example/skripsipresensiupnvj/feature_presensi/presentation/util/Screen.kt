package com.example.skripsipresensiupnvj.feature_presensi.presentation.util

sealed class Screen(val route: String) {
    object LoginScreen: Screen("login_screen")
    object HomeUserScreen: Screen("home_user_screen")
    object HomeAdminScreen: Screen("home_admin_screen")
    object ProfileScreen: Screen("profile_screen")
    object AddKegiatanScreen: Screen("add_kegiatan_screen")
    object DetailKegiatanScreen: Screen("detail_kegiatan_screen")
    object AlasanScreen: Screen("alasan_screen")
    object RegisterScreen: Screen ("register_screen")
    object ConfirmPesertaScreen: Screen ("confirm_peserta_screen")
}