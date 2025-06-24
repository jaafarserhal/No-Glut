package com.example.noglut.network.base

import com.example.noglut.network.user.models.LoginRequest
import com.example.noglut.network.user.models.RegisterRequest
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
}