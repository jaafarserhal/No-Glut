package com.example.noglut.network.base

import android.content.Context
import com.example.noglut.network.user.models.LoginResponse
import androidx.core.content.edit

class SessionManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_LOGIN_TIMESTAMP = "login_timestamp"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveUserSession(loginResponse: LoginResponse) {
        sharedPreferences.edit {
            putString(KEY_AUTH_TOKEN, loginResponse.token)
            putString(KEY_USER_ID, loginResponse.userId)
            putString(KEY_USER_EMAIL, loginResponse.email)
            putString(KEY_USER_NAME, "${loginResponse.firstName} ${loginResponse.lastName}")
            putLong(KEY_LOGIN_TIMESTAMP, System.currentTimeMillis())
            putBoolean(KEY_IS_LOGGED_IN, true)
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }

    fun getLoginTimestamp(): Long {
        return sharedPreferences.getLong(KEY_LOGIN_TIMESTAMP, 0)
    }

    fun clearSession() {
        sharedPreferences.edit { clear() }
    }

    // Check if session is still valid (optional - based on your token expiry logic)
    fun isSessionValid(): Boolean {
        if (!isLoggedIn()) return false

        val loginTime = getLoginTimestamp()
        val currentTime = System.currentTimeMillis()
        val sessionDuration = currentTime - loginTime

        // Example: Session expires after 7 days (adjust as needed)
        val maxSessionDuration = 7 * 24 * 60 * 60 * 1000L // 7 days in milliseconds

        return sessionDuration < maxSessionDuration
    }
}