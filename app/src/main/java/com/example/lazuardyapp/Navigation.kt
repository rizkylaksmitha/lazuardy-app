package com.example.lazuardyapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lazuardyapp.ui.screens.LoginScreen
import com.example.lazuardyapp.ui.screens.RegisterScreen
import com.example.lazuardyapp.ui.screens.SplashScreen
import com.example.lazuardyapp.ui.screens.JadwalScreen
import com.example.lazuardyapp.ui.screens.ProfileScreen
import com.example.lazuardyapp.ui.screens.EditProfileScreen
import com.example.lazuardyapp.ui.screens.InitialEmptyProfile


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    var userProfileState by remember { mutableStateOf(InitialEmptyProfile) }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = {
                    val userEmailFromLogin = "email_pengguna@contoh.com"
                    val userPhoneFromLogin = "(+62) 81234567890"

                    userProfileState = userProfileState.copy(
                        email = userEmailFromLogin,
                        telepon = userPhoneFromLogin,
                        nomorWhatsApp = if (userProfileState.nomorWhatsApp.isBlank() || userProfileState.nomorWhatsApp == InitialEmptyProfile.telepon) userPhoneFromLogin else userProfileState.nomorWhatsApp
                    )

                    navController.navigate("jadwal") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("jadwal") {
            JadwalScreen(
                onNavigateToHome = {
                    navController.popBackStack("jadwal", inclusive = false)
                },
                onNavigateToJadwal = {
                    navController.popBackStack("jadwal", inclusive = false)
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                userProfile = userProfileState,
                onNavigateToHome = {
                    navController.navigate("jadwal") {
                        popUpTo("jadwal") { inclusive = true }
                    }
                },
                onNavigateToJadwal = {
                    navController.navigate("jadwal") {
                        popUpTo("jadwal") { inclusive = true }
                    }
                },
                onNavigateToEditProfile = {
                    navController.navigate("editProfile")
                }
            )
        }

        composable("editProfile") {
            EditProfileScreen(
                currentProfile = userProfileState,
                onSaveProfile = { updatedProfile ->
                    userProfileState = updatedProfile.copy(
                        email = userProfileState.email,
                        telepon = userProfileState.telepon
                    )
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}