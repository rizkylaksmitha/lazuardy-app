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
import com.example.lazuardyapp.dashboard.DashboardScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.lazuardyapp.tutorselection.TutorSelectionScreen
// Pastikan Tutor dan TutorSelectionScreen diimpor dengan benar dari package-nya
// Jika Anda menempatkan Tutor data class di package com.example.lazuardyapp.tutorselection
// import com.example.lazuardyapp.tutorselection.Tutor


@Composable
fun Navigation() { // Nama fungsi diganti dari AppNavigation menjadi Navigation
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

                    navController.navigate("dashboard") {
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

        composable("dashboard") {
            DashboardScreen(
                onNavigateToJadwal = {
                    navController.navigate("jadwal") {
                        popUpTo("dashboard") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate("profile") {
                        popUpTo("dashboard") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                // TAMBAHAN: Navigasi ke Tutor Selection
                onNavigateToTutorSelection = { subjectName ->
                    navController.navigate("tutorSelection/$subjectName")
                }
            )
        }

        composable("jadwal") {
            JadwalScreen(
                onNavigateToHome = {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onNavigateToJadwal = {
                },
                onNavigateToProfile = {
                    navController.navigate("profile") {
                        popUpTo("dashboard") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                userProfile = userProfileState,
                onNavigateToHome = {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onNavigateToJadwal = {
                    navController.navigate("jadwal") {
                        popUpTo("dashboard") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
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

        // RUTE BARU: Tutor Selection
        composable(
            route = "tutorSelection/{subjectName}",
            arguments = listOf(navArgument("subjectName") { type = NavType.StringType })
        ) { backStackEntry ->
            val subjectName = backStackEntry.arguments?.getString("subjectName") ?: "Semua Pelajaran"

            TutorSelectionScreen(
                onNavigateBack = { navController.popBackStack() },
                onTutorSelected = { selectedTutor ->
                    // Logika setelah tutor dipilih, misalnya navigasi ke detail paket
                    println("Tutor ${selectedTutor.name} (${selectedTutor.subject}) dipilih!")
                    // Contoh: navController.navigate("packageDetail/${selectedTutor.id}")
                },
                selectedSubjectName = subjectName
            )
        }
    }
}