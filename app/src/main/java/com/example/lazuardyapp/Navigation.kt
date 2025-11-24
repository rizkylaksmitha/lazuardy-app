package com.example.lazuardyapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lazuardyapp.ui.screens.LoginScreen
import com.example.lazuardyapp.ui.screens.RegisterScreen
import com.example.lazuardyapp.ui.screens.SplashScreen
import com.example.lazuardyapp.ui.screens.JadwalScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

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
                    navController.navigate("jadwal") {
                        popUpTo("login") {inclusive = true}
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
                    // Nanti navigate ke dashboard/home
                },
                onNavigateToProfile = {
                    // Nanti navigate ke profil
                }
            )
        }
    }
}