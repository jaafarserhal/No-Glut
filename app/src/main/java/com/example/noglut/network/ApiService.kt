package com.example.noglut.network

import com.example.noglut.network.user.models.ApiResponse
import com.example.noglut.network.user.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("Authentication/signup")
    @Headers("Content-Type: application/json")
    suspend fun registerUser(@Body request: RegisterRequest): Response<ApiResponse>
}