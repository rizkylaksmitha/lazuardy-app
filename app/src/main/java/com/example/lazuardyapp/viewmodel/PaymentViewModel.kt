package com.example.lazuardyapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lazuardyapp.SelectedPackageData
import com.example.lazuardyapp.data.SessionManager
import com.example.lazuardyapp.data.model.ActivePackage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class PaymentViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application.applicationContext)
    private val gson = Gson()

    var isProcessing by mutableStateOf(false)
        private set

    var selectedPackageData by mutableStateOf<SelectedPackageData?>(null)
        private set

    fun confirmTransfer(
        packageId: Int,
        packageName: String,
        totalSessions: Int,
        tutorName: String,
        subject: String,
        scheduleDays: List<String>,
        onSuccess: () -> Unit
    ) {
        if (isProcessing) return
        isProcessing = true

        val dataToConfirm = selectedPackageData ?: run {
            isProcessing = false
            return
        }

        viewModelScope.launch {
            delay(2000)

            val listType: Type = object : TypeToken<List<ActivePackage>>() {}.type

            val existingJson = sessionManager.getActivePackageJson().firstOrNull()

            val existingPackages: List<ActivePackage> = try {
                if (existingJson.isNullOrEmpty()) emptyList() else gson.fromJson(existingJson, listType)
            } catch (e: Exception) {
                println("GSON Error reading existing packages: ${e.message}")
                emptyList()
            }

            val newActivePackage = ActivePackage(
                packageName = dataToConfirm.packageName,
                totalSessions = dataToConfirm.totalSessions,
                currentProgress = 0,
                subject = dataToConfirm.subject,
                tutorName = dataToConfirm.tutorName,
                packageId = dataToConfirm.packageId,
                scheduleDays = dataToConfirm.scheduleDays
            )

            val updatedPackages = existingPackages + newActivePackage
            val jsonToSave = gson.toJson(updatedPackages)

            sessionManager.saveActivePackageJson(jsonToSave)

            isProcessing = false
            onSuccess()
        }
    }

    fun setPackageForPayment(data: SelectedPackageData) {
        selectedPackageData = data
    }
}