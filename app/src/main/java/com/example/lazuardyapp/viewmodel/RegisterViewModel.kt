package com.example.lazuardyapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lazuardyapp.data.model.RegisterRequest
import com.example.lazuardyapp.network.MockClient
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var registerStatusMessage by mutableStateOf("")

    private val mockClient = MockClient(application.applicationContext)

    fun performRegister(onSuccess: () -> Unit) {
        if (isLoading) return

        if (password.length < 8) {
            registerStatusMessage = "Kata sandi minimal 8 karakter."
            return
        }
        if (password != confirmPassword) {
            registerStatusMessage = "Kata sandi dan konfirmasi tidak sama."
            return
        }
        if (name.isBlank() || email.isBlank()) {
            registerStatusMessage = "Semua field harus diisi."
            return
        }

        isLoading = true
        registerStatusMessage = ""

        viewModelScope.launch {
            try {
                val request = RegisterRequest(name, email, password, confirmPassword)
                val response = mockClient.register(request)

                if (response.isSuccessful) {
                    registerStatusMessage = response.body()?.message ?: "Pendaftaran Berhasil!"
                    onSuccess()
                } else {
                    registerStatusMessage = "Pendaftaran Gagal: Email sudah terdaftar."
                }
            } catch (e: Exception) {
                registerStatusMessage = "Terjadi kesalahan koneksi (Mock Error): ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}