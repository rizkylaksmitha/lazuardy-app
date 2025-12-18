package com.example.lazuardyapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lazuardyapp.data.SessionManager
import com.example.lazuardyapp.data.model.LoginRequest
import com.example.lazuardyapp.network.MockClient
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var rememberMe by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
    var loginStatusMessage by mutableStateOf("")

    private val mockClient = MockClient(application.applicationContext)
    private val sessionManager = SessionManager(application.applicationContext)

    fun performLogin(onSuccess: () -> Unit) {
        if (isLoading) return

        if (email.isBlank() || password.isBlank()) {
            loginStatusMessage = "Email dan password tidak boleh kosong."
            return
        }

        isLoading = true
        loginStatusMessage = ""

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = mockClient.login(request)

                if (response.isSuccessful) {
                    val token = response.body()?.token

                    if (rememberMe && token != null) {
                        sessionManager.saveAuthToken(token)
                    } else if (token != null) { }

                    loginStatusMessage = response.body()?.message ?: "Login Berhasil!"
                    onSuccess()
                } else {
                    loginStatusMessage = "Login Gagal: Cek Email dan Password Anda."
                }
            } catch (e: Exception) {
                loginStatusMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}