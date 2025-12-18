package com.example.lazuardyapp.network

import android.content.Context
import com.example.lazuardyapp.data.SessionManager
import com.example.lazuardyapp.data.model.LoginRequest
import com.example.lazuardyapp.data.model.LoginResponse
import com.example.lazuardyapp.data.model.RegisterRequest
import com.example.lazuardyapp.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

data class RegisterResponse(
    val status: String,
    val message: String
)

interface ApiService {
    suspend fun login(request: LoginRequest): Response<LoginResponse>
    suspend fun register(request: RegisterRequest): Response<RegisterResponse>
}

class MockClient(private val context: Context) : ApiService {

    private val sessionManager = SessionManager(context)
    private val VALID_EMAIL = "user@test.com"
    private val VALID_PASSWORD = "password123"

    override suspend fun login(request: LoginRequest): Response<LoginResponse> {
        delay(1500L)

        val registeredEmail = sessionManager.getUserEmail().firstOrNull()
        val registeredPassword = sessionManager.getMockPassword().firstOrNull()
        val registeredName = sessionManager.getUserName().firstOrNull()

        val validEmail = registeredEmail ?: VALID_EMAIL
        val validPassword = registeredPassword ?: VALID_PASSWORD
        val validName = registeredName ?: "Default User"

        return if (request.email == validEmail && request.password == validPassword) {
            val mockUser = User(id = 1, name = validName, email = request.email)
            val mockResponse = LoginResponse(
                status = "success",
                message = "Login Berhasil: Halo, $validName!",
                token = "mock_token_12345",
                user = mockUser
            )
            sessionManager.saveAuthToken("mock_token_success")
            Response.success(mockResponse)
        } else {
            val errorJson = "{\"status\":\"error\", \"message\":\"Email atau password salah (Mock)\"}"
            Response.error(
                400,
                errorJson.toResponseBody("application/json".toMediaTypeOrNull())
            )
        }
    }
    override suspend fun register(request: RegisterRequest): Response<RegisterResponse> {
        delay(2000L)

        if (request.email == VALID_EMAIL) {
            val errorJson = "{\"status\":\"error\", \"message\":\"Email sudah terdaftar (Mock)\"}"
            return Response.error(
                409, // Conflict
                errorJson.toResponseBody("application/json".toMediaTypeOrNull())
            )
        }

        sessionManager.saveProfileData(
            name = request.name,
            email = request.email,
            password = request.password
        )

        val mockResponse = RegisterResponse(
            status = "success",
            message = "Pendaftaran Berhasil! Silakan masuk."
        )
        return Response.success(mockResponse)
    }
}