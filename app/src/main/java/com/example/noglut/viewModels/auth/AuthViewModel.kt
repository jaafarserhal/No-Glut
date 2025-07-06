package com.example.noglut.viewModels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noglut.network.base.ApiResponse
import com.example.noglut.network.user.models.RegisterRequest
import com.example.noglut.network.user.UserRepository
import com.example.noglut.network.user.models.LoginRequest
import com.example.noglut.network.user.models.ResetPasswordRequest
import com.example.noglut.network.user.models.SendResetCodeRequest
import com.example.noglut.network.user.models.VerifyResetCodeRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel : ViewModel() {
    private val repository = UserRepository()

    fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: (ApiResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val signupRequest = RegisterRequest(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )

        viewModelScope.launch {
            repository.registerUser(signupRequest)
                .onSuccess { response ->
                    onSuccess(response)
                }
                .onFailure { error ->
                    if (error is HttpException) {
                        val errorBody = error.response()?.errorBody()?.string()
                        onError("HTTP ${error.code()}: $errorBody")
                    } else {
                        onError(error.message ?: "Unknown error occurred")
                    }
                }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: (ApiResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val loginRequest = LoginRequest(
            email = email,
            password = password
        )

        viewModelScope.launch {
            repository.login(loginRequest)
                .onSuccess { response ->
                    onSuccess(response)
                }
                .onFailure { error ->
                    if (error is HttpException) {
                        val errorBody = error.response()?.errorBody()?.string()
                        onError("HTTP ${error.code()}: $errorBody")
                    } else {
                        onError(error.message ?: "Unknown error occurred")
                    }
                }
        }
    }

    fun sendResetPasswordCode(
        email: String,
        onSuccess: (ApiResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = SendResetCodeRequest(
            email = email
        )
        viewModelScope.launch {
            repository.sendResetPasswordCode(request)
                .onSuccess { response ->
                    onSuccess(response)
                }
                .onFailure { error ->
                    if (error is HttpException) {
                        val errorBody = error.response()?.errorBody()?.string()
                        onError("HTTP ${error.code()}: $errorBody")
                    } else {
                        onError(error.message ?: "Unknown error occurred")
                    }
                }
        }
    }

    fun verifyResetPasswordCode(
        email: String,
        resetCode: String,
        onSuccess: (ApiResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = VerifyResetCodeRequest(
            email = email,
            resetCode = resetCode
        )
        viewModelScope.launch {
            repository.verifyResetPasswordCode(request)
                .onSuccess { response ->
                    onSuccess(response)
                }
                .onFailure { error ->
                    if (error is HttpException) {
                        val errorBody = error.response()?.errorBody()?.string()
                        onError("HTTP ${error.code()}: $errorBody")
                    } else {
                        onError(error.message ?: "Unknown error occurred")
                    }
                }
        }
    }

    fun resetPassword(
        email: String,
        resetCode: String,
        newPassword: String,
        confirmPassword: String,
        onSuccess: (ApiResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = ResetPasswordRequest(
            email = email,
            resetCode = resetCode,
            newPassword = newPassword,
            confirmPassword = confirmPassword
        )
        viewModelScope.launch {
            repository.resetPassword(request)
                .onSuccess { response ->
                    onSuccess(response)
                }
                .onFailure { error ->
                    if (error is HttpException) {
                        val errorBody = error.response()?.errorBody()?.string()
                        onError("HTTP ${error.code()}: $errorBody")
                    } else {
                        onError(error.message ?: "Unknown error occurred")
                    }
                }
        }
    }

}
