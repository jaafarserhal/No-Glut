package com.example.noglut.network.user.models

import com.google.gson.annotations.SerializedName

data class SendResetCodeRequest(
    @SerializedName("email") val email: String
)