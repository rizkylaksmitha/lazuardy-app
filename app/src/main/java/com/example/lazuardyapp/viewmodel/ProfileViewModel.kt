package com.example.lazuardyapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lazuardyapp.data.SessionManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import com.example.lazuardyapp.ui.screens.UserProfile
import com.example.lazuardyapp.ui.screens.createInitialProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application.applicationContext)
    private val gson = Gson()

    var profileData by mutableStateOf(getInitialProfileData())
    var isLoading by mutableStateOf(false)
    var statusMessage by mutableStateOf("")

    private val _navigateToLogin = Channel<Boolean>(Channel.BUFFERED)
    val navigateToLogin = _navigateToLogin.receiveAsFlow()

    init {
        loadProfile()
    }

    private fun getInitialProfileData(): UserProfile {
        return createInitialProfile(email = "loading@email.com", telepon = "")
    }

    fun loadProfile() {
        isLoading = true
        statusMessage = ""
        viewModelScope.launch {
            val savedName = sessionManager.getUserName().firstOrNull() ?: "Pengguna Baru"
            val savedEmail = sessionManager.getUserEmail().firstOrNull() ?: "email@lazuardy.com"
            val savedPhone = sessionManager.getUserPhone().firstOrNull() ?: ""

            val jsonString = sessionManager.getProfileJson().firstOrNull()

            val defaultProfile = createInitialProfile(email = savedEmail, telepon = savedPhone).copy(
                namaLengkap = savedName,
                email = savedEmail,
                telepon = savedPhone,
                nomorWhatsApp = savedPhone
            )

            if (jsonString.isNullOrEmpty()) {
                profileData = defaultProfile
            } else {
                try {
                    profileData = gson.fromJson(jsonString, UserProfile::class.java)
                } catch (e: Exception) {
                    statusMessage = "Gagal memuat data profil lama."
                    profileData = defaultProfile
                }
            }
            isLoading = false
        }
    }

    fun saveProfile(newProfile: UserProfile, onSuccess: () -> Unit) {
        isLoading = true
        statusMessage = ""
        viewModelScope.launch {
            try {
                val jsonString = gson.toJson(newProfile)
                sessionManager.saveProfileJson(jsonString)
                profileData = newProfile
                statusMessage = "Profil berhasil disimpan!"
                delay(500)
                onSuccess()
            } catch (e: Exception) {
                statusMessage = "Gagal menyimpan profil: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        isLoading = true
        statusMessage = ""
        viewModelScope.launch {
            try {
                sessionManager.logout()

                delay(100)

                profileData = getInitialProfileData()

                _navigateToLogin.send(true)
            } catch (e: Exception) {
                statusMessage = "Gagal Logout: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}