package com.example.lazuardyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.lazuardyapp.ui.theme.LazuardyAppTheme

// PENTING: Anda harus mengimpor fungsi Navigation() dari file yang sesuai.
// Asumsi: Fungsi 'Navigation' berada di package com.example.lazuardyapp
// Jika fungsi 'Navigation' berada di package root, import ini tidak diperlukan.
// Namun, jika berada di file terpisah bernama 'Navigation.kt' dalam package yang sama, ini sudah otomatis.
// Jika fungsi Navigation berada di package lain, ganti dengan:
// import com.example.lazuardyapp.navigation.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Baris 15 dan 17 adalah lokasi error
        setContent {
            LazuardyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Masalah terletak di sini.
                    // Fungsi Navigation() harus didefinisikan sebagai @Composable di tempat lain.
                    // Jika error terus berlanjut, Anda perlu membersihkan (Clean) dan membangun ulang (Rebuild) proyek.
                    Navigation()
                }
            }
        }
    }
}