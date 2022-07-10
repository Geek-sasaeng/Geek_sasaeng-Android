package com.example.geeksasaeng.Data

import com.google.gson.annotations.SerializedName

data class EmailCheck(
    @SerializedName("email") var email: String? = "",
    @SerializedName("check") var check: Int? = -1,
)