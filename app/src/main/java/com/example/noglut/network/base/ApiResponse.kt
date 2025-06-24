package com.example.noglut.network.base

data class ApiResponse(
    val isSuccess: Boolean,
    val data: Any?,
    val message: String?,
    val statusCode: Int?
)
