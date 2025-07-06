package com.example.noglut.network.base

import com.example.noglut.network.user.models.LoginRequest
import com.example.noglut.network.user.models.RegisterRequest
import com.example.noglut.network.user.models.ResetPasswordRequest
import com.example.noglut.network.user.models.SendResetCodeRequest
import com.example.noglut.network.user.models.VerifyResetCodeRequest
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("Authentication/register")
    @Headers("Content-Type: application/json")
    suspend fun registerUser(@Body request: RegisterRequest): Response<ApiResponse>

    @POST("Authentication/login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse>

    @POST("Authentication/send-reset-code")
    @Headers("Content-Type: application/json")
    suspend fun sendResetPasswordCode(@Body request: SendResetCodeRequest): Response<ApiResponse>

    @POST("Authentication/verify-code")
    @Headers("Content-Type: application/json")
    suspend fun verifyResetPasswordCode(@Body request: VerifyResetCodeRequest): Response<ApiResponse>

    @POST("Authentication/reset-password")
    @Headers("Content-Type: application/json")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ApiResponse>
}