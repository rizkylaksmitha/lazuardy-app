package com.example.lazuardyapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument

import com.example.lazuardyapp.dashboard.DashboardScreen
import com.example.lazuardyapp.tutorselection.TutorSelectionScreen
import com.example.lazuardyapp.pilihpaket.PilihPaketBelajarScreen
import com.example.lazuardyapp.ui.screens.LoginScreen
import com.example.lazuardyapp.ui.screens.RegisterScreen


object Screens {
    const val LOGIN = "login_screen"
    const val REGISTER = "register_screen"
    const val DASHBOARD = "dashboard_screen"
    const val PROFILE = "profile_screen"
    const val JADWAL = "schedule_screen"
    const val TUTOR_SELECTION = "tutor_selection_screen/{subjectName}"
    const val PILIH_PAKET = "pilih_paket_belajar_screen/{tutorId}"
}


@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.LOGIN
    ) {

        composable(Screens.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screens.DASHBOARD) {
                        popUpTo(Screens.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screens.REGISTER) }
            )
        }

        composable(Screens.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screens.DASHBOARD) {
                        popUpTo(Screens.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        composable(Screens.DASHBOARD) {
            DashboardScreen(
                onNavigateToJadwal = { navController.navigate(Screens.JADWAL) },
                onNavigateToProfile = { navController.navigate(Screens.PROFILE) },
                onNavigateToTutorSelection = { subjectName ->
                    navController.navigate(Screens.TUTOR_SELECTION.replace("{subjectName}", subjectName))
                }
            )
        }

        composable(
            route = Screens.TUTOR_SELECTION,
            arguments = listOf(navArgument("subjectName") { type = NavType.StringType })
        ) { backStackEntry ->
            val selectedSubject = backStackEntry.arguments?.getString("subjectName") ?: "Semua Pelajaran"

            TutorSelectionScreen(
                onNavigateBack = { navController.popBackStack() },
                selectedSubjectName = selectedSubject,
                onTutorSelected = { tutor ->
                    navController.navigate(Screens.PILIH_PAKET.replace("{tutorId}", tutor.id.toString()))
                }
            )
        }

        composable(
            route = Screens.PILIH_PAKET,
            arguments = listOf(navArgument("tutorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tutorId = backStackEntry.arguments?.getInt("tutorId") ?: 0

            PilihPaketBelajarScreen(
                tutorId = tutorId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screens.PROFILE) {
            /* Isi dengan ProfileScreen() Anda */
        }
        composable(Screens.JADWAL) {
            /* Isi dengan JadwalScreen() */
        }
    }
}