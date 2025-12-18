package com.example.lazuardyapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.app.Application
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lazuardyapp.dashboard.DashboardScreen
import com.example.lazuardyapp.pilihpaket.PilihPaketBelajarScreen
import com.example.lazuardyapp.tutorselection.TutorSelectionScreen
import com.example.lazuardyapp.ui.screens.EditProfileScreen
import com.example.lazuardyapp.ui.screens.InitialEmptyProfile
import com.example.lazuardyapp.ui.screens.JadwalScreen
import com.example.lazuardyapp.ui.screens.LoginScreen
import com.example.lazuardyapp.ui.screens.ProfileScreen
import com.example.lazuardyapp.ui.screens.RegisterScreen
import com.example.lazuardyapp.ui.screens.SplashScreen
import com.example.lazuardyapp.ui.screens.UserProfile
import com.example.lazuardyapp.viewmodel.PaymentViewModel
import com.example.lazuardyapp.viewmodel.ProfileViewModel
import com.google.gson.Gson

object Screens {
    const val SPLASH = "splash_screen"
    const val LOGIN = "login_screen"
    const val REGISTER = "register_screen"
    const val DASHBOARD = "dashboard_screen"
    const val PROFILE = "profile_screen"
    const val JADWAL = "schedule_screen"
    const val TUTOR_SELECTION = "tutor_selection_screen/{subjectName}"
    const val PILIH_PAKET = "pilih_paket_belajar_screen/{tutorId}"
    const val PAYMENT = "payment_screen"
    const val EDIT_PROFILE = "edit_profile_screen/{profileJson}"
}

@Composable
fun Navigation(startDestination: String) {
    val navController = rememberNavController()
    val gson = Gson()
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val appViewModelFactory = rememberAndroidViewModelFactory(application)

    NavHost(
        navController = navController,
        startDestination = Screens.SPLASH
    ) {

        composable(Screens.SPLASH) {
            SplashScreen(
                onAnimationFinished = {
                    navController.navigate(Screens.LOGIN) {
                        popUpTo(Screens.SPLASH) {inclusive = true}
                    }
                }
            )
        }

        composable(Screens.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screens.REGISTER) },
                onNavigateToDashboard = {
                    navController.navigate(Screens.DASHBOARD) {
                        popUpTo(Screens.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Screens.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(Screens.LOGIN) },
                onNavigateToDashboard = {
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

        composable(Screens.JADWAL) {
            JadwalScreen(
                onNavigateToHome = { navController.navigate(Screens.DASHBOARD) },
                onNavigateToJadwal = {  },
                onNavigateToProfile = { navController.navigate(Screens.PROFILE) }
            )
        }

        composable(Screens.PROFILE) {
            val profileViewModel: ProfileViewModel = viewModel(factory = appViewModelFactory)

            LaunchedEffect(Unit) {
                profileViewModel.navigateToLogin.collect { shouldNavigate ->
                    if (shouldNavigate) {
                        navController.navigate(Screens.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }

            ProfileScreen(
                viewModel = profileViewModel,
                onNavigateToHome = { navController.navigate(Screens.DASHBOARD) },
                onNavigateToJadwal = { navController.navigate(Screens.JADWAL) },
                onNavigateToProfile = { /* Sudah di halaman profile */ },
                onNavigateToEditProfile = { userProfile ->
                    val profileJson = gson.toJson(userProfile)
                    val encodedJson = java.net.URLEncoder.encode(
                        profileJson,
                        java.nio.charset.StandardCharsets.UTF_8.toString()
                    )
                    navController.navigate(Screens.EDIT_PROFILE.replace("{profileJson}", encodedJson))
                }
            )
        }

        composable(
            route = Screens.EDIT_PROFILE,
            arguments = listOf(navArgument("profileJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("profileJson") ?: ""
            val profileJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
            val profileViewModel: ProfileViewModel = viewModel(factory = appViewModelFactory)

            val initialProfile = try {
                gson.fromJson(profileJson, UserProfile::class.java)
            } catch (e: Exception) {
                InitialEmptyProfile
            }

            EditProfileScreen(
                currentProfile = initialProfile,
                onSaveProfile = {
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() },
                viewModel = profileViewModel
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
            val paymentViewModel: PaymentViewModel = viewModel(factory = appViewModelFactory)
            PilihPaketBelajarScreen(
                tutorId = tutorId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPayment = { navController.navigate(Screens.PAYMENT) },
                paymentViewModel = paymentViewModel
            )
        }

        composable(route = Screens.PAYMENT) {
            val previousEntry = navController.previousBackStackEntry
            val paymentViewModel: PaymentViewModel = if (previousEntry != null) {
                viewModel(viewModelStoreOwner = previousEntry, factory = appViewModelFactory)
            } else {
                viewModel(factory = appViewModelFactory)
            }
            PaymentScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screens.DASHBOARD) {
                        popUpTo(Screens.DASHBOARD) { inclusive = true }
                    }
                },
                paymentViewModel = paymentViewModel
            )
        }
    }
}

@Composable
fun rememberAndroidViewModelFactory(application: Application): ViewModelProvider.Factory {
    return remember {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                try {
                    return modelClass.getConstructor(Application::class.java).newInstance(application)
                } catch (e: Exception) {
                    throw RuntimeException("Cannot create instance of $modelClass", e)
                }
            }
        }
    }
}