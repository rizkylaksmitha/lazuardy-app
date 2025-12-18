package com.example.lazuardyapp

data class SelectedPackageData(
    val packageId: Int,
    val packageName: String,
    val totalSessions: Int,
    val tutorName: String,
    val subject: String,
    val scheduleDays: List<String>
)