package com.example.lazuardyapp.data.model

data class ActivePackage(
    val packageName: String,
    val totalSessions: Int,
    val currentProgress: Int,
    val subject: String,
    val tutorName: String,
    val packageId: Int,
    val scheduleDays: List<String>
)