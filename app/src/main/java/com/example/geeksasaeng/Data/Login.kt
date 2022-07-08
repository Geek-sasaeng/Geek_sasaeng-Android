package com.example.geeksasaeng.Data

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("loginId") var loginId: String? = "",
    @SerializedName("password") var password: String? = ""
)
