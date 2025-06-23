package com.example.noglut.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noglut.network.user.models.ApiResponse
import com.example.noglut.network.user.models.RegisterRequest
import com.example.noglut.network.user.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {
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
}
