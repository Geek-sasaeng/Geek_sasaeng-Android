package com.example.geeksasaeng.Data

import com.google.gson.annotations.SerializedName

data class Signup(
    @SerializedName("checkPassword") val checkPassword: String,
    @SerializedName("email") val email: String,
    @SerializedName("loginId") val loginId: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("password") val password: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("universityName") val universityName: String
)