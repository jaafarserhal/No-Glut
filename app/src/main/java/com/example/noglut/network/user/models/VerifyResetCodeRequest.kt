package com.example.noglut.network.user.models

import com.google.gson.annotations.SerializedName

data class VerifyResetCodeRequest(
    @SerializedName("email") val email: String,
    @SerializedName("resetCode") val resetCode: String
)
