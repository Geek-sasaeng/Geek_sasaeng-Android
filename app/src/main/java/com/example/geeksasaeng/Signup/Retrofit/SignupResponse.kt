package com.example.geeksasaeng.Signup.Retrofit

import com.google.gson.annotations.SerializedName

data class SignupResult(
    // @SerializedName("checkPassword") val checkPassword: String,
    @SerializedName("email") val email: String,
    @SerializedName("loginId") val loginId: String,
    @SerializedName("nickname") val nickname: String,
    // @SerializedName("password") val password: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("universityName") val universityName: String
)

data class SignupResponse(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SignupResult?
)