package com.example.lazuardyapp.viewmodel

import android.app.Application
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lazuardyapp.data.SessionManager
import com.example.lazuardyapp.data.model.ActivePackage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.reflect.Type

data class PackageSummary(
    val totalActivePackages: Int,
    val totalSessionsBought: Int,
    val totalSessionsCompleted: Int
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application.applicationContext)
    private val gson = Gson()

    val userName = sessionManager.getUserName()
        .map { name -> name ?: "Pengguna" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Memuat..."
        )

    var activePackageData by mutableStateOf<List<ActivePackage>>(emptyList())
    var isUserActive by mutableStateOf<Boolean>(false)
    var isLoading by mutableStateOf(true)

    val packageSummary by derivedStateOf {
        PackageSummary(
            totalActivePackages = activePackageData.size,
            totalSessionsBought = activePackageData.sumOf { it.totalSessions },
            totalSessionsCompleted = activePackageData.sumOf { it.currentProgress }
        )
    }

    init {
        loadActivePackage()
    }

    fun loadActivePackage() {
        isLoading = true
        viewModelScope.launch {
            val listType: Type = object : TypeToken<List<ActivePackage>>() {}.type

            sessionManager.getActivePackageJson().collect { jsonString ->

                val loadedList: List<ActivePackage> = if (jsonString.isNullOrEmpty()) {
                    emptyList()
                } else {
                    try {
                        gson.fromJson(jsonString, listType) ?: emptyList()
                    } catch (e: Exception) {
                        emptyList()
                    }
                }

                activePackageData = loadedList
                isUserActive = loadedList.isNotEmpty()
                isLoading = false
            }
        }
    }
}