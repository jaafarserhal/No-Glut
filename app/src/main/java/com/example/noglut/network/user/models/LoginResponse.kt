package com.example.noglut.network.user.models

data class LoginResponse(
    val token: String = "",
    val userId: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = ""
)

