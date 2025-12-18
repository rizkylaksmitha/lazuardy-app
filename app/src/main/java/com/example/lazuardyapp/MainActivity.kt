package com.example.lazuardyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.lazuardyapp.data.SessionManager
import com.example.lazuardyapp.ui.theme.LazuardyAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            LazuardyAppTheme {
                var startDestination by remember { mutableStateOf<String?>(null) }
                splashScreen.setKeepOnScreenCondition { startDestination == null }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val sessionManager = remember { SessionManager(context) }

                    LaunchedEffect(Unit) {
                        val token = sessionManager.getAuthToken().first()
                        startDestination = if (token.isEmpty()) Screens.LOGIN else Screens.DASHBOARD
                        delay(500)
                    }

                    if (startDestination != null) {
                        Navigation(startDestination = startDestination!!)
                    }
                }
            }
        }
    }
}