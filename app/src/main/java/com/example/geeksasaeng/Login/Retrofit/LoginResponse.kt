package com.example.geeksasaeng.Login.Retrofit

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("jwt") val jwt: String
)

data class LoginResponse(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: LoginResult?
)