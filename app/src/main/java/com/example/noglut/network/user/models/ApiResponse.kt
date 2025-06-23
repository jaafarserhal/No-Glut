package com.example.noglut.network.user.models

data class ApiResponse(
    val isSuccess: Boolean,
    val data: Any?,
    val message: String?,
    val statusCode: Int?
)
