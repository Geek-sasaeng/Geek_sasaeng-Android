package com.example.geeksasaeng.Login.Retrofit

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("jwt") val jwt: String,
    @SerializedName("loginStatus") val loginStatus: String,
    @SerializedName("nickName") val nickName: String
)

data class LoginResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: LoginResult?
)

data class Login(
    @SerializedName("loginId") var loginId: String? = "",
    @SerializedName("password") var password: String? = ""
)

