package com.example.lazuardyapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class SessionManager(private val context: Context) {
    companion object {
        val USER_TOKEN_KEY = stringPreferencesKey("user_token")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val MOCK_PASSWORD_KEY = stringPreferencesKey("mock_password")
        val ACTIVE_PACKAGE_JSON_KEY = stringPreferencesKey("active_package_json")
        val PROFILE_DATA_JSON_KEY = stringPreferencesKey("profile_data_json")
        val USER_PHONE_KEY = stringPreferencesKey("user_phone")
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { it[USER_TOKEN_KEY] = token }
    }

    fun getAuthToken(): Flow<String> = context.dataStore.data.map { it[USER_TOKEN_KEY] ?: "" }

    suspend fun logout() {
        context.dataStore.edit { it.clear() }
    }

    fun getUserName(): Flow<String?> = context.dataStore.data.map { it[USER_NAME_KEY] }
    fun getUserEmail(): Flow<String?> = context.dataStore.data.map { it[USER_EMAIL_KEY] }
    fun getActivePackageJson(): Flow<String?> = context.dataStore.data.map { it[ACTIVE_PACKAGE_JSON_KEY] }
    fun getProfileJson(): Flow<String?> = context.dataStore.data.map { it[PROFILE_DATA_JSON_KEY] }
    fun getUserPhone(): Flow<String?> = context.dataStore.data.map { it[USER_PHONE_KEY] }
    fun getMockPassword(): Flow<String?> = context.dataStore.data.map { it[MOCK_PASSWORD_KEY] }

    suspend fun saveProfileData(name: String, email: String, password: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
            preferences[USER_EMAIL_KEY] = email
            preferences[MOCK_PASSWORD_KEY] = password
            preferences[USER_TOKEN_KEY] = "new_user_token_${System.currentTimeMillis()}"
        }
    }

    suspend fun saveProfileJson(jsonString: String) {
        context.dataStore.edit { it[PROFILE_DATA_JSON_KEY] = jsonString }
    }

    suspend fun saveActivePackageJson(jsonString: String) {
        context.dataStore.edit { it[ACTIVE_PACKAGE_JSON_KEY] = jsonString }
    }

    suspend fun saveUserPhone(phone: String) {
        context.dataStore.edit { it[USER_PHONE_KEY] = phone }
    }
}