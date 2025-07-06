package com.example.noglut.network.user

import com.example.noglut.network.base.ApiResponse
import com.example.noglut.network.base.NetworkModule
import com.example.noglut.network.user.models.LoginRequest
import com.example.noglut.network.user.models.RegisterRequest
import com.example.noglut.network.user.models.ResetPasswordRequest
import com.example.noglut.network.user.models.SendResetCodeRequest
import com.example.noglut.network.user.models.VerifyResetCodeRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class UserRepository {
    private val apiService = NetworkModule.apiService

    suspend fun registerUser(request: RegisterRequest): Result<ApiResponse> {
        return handleNetworkCall { apiService.registerUser(request) }
    }

    suspend fun login(request: LoginRequest): Result<ApiResponse> {
        return handleNetworkCall { apiService.login(request) }
    }

    suspend fun sendResetPasswordCode(request: SendResetCodeRequest): Result<ApiResponse> {
        return handleNetworkCall { apiService.sendResetPasswordCode(request) }
    }

    suspend fun verifyResetPasswordCode(request: VerifyResetCodeRequest): Result<ApiResponse> {
        return handleNetworkCall { apiService.verifyResetPasswordCode(request) }
    }

    suspend fun resetPassword(request: ResetPasswordRequest): Result<ApiResponse> {
        return handleNetworkCall { apiService.resetPassword(request) }
    }


    private suspend fun handleNetworkCall(apiCall: suspend () -> Response<ApiResponse>): Result<ApiResponse> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timeout. Please check your internet connection and try again."))
        } catch (e: UnknownHostException) {
            Result.failure(Exception("No internet connection. Please check your network settings."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error occurred. Please try again."))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.message}"))
        }
    }
}
