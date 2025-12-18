package com.example.lazuardyapp.data.model

data class LoginResponse(
    val status: String,
    val message: String,
    val token: String?,
    val user: User?
)