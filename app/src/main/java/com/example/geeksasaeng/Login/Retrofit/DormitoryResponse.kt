package com.example.geeksasaeng.Login.Retrofit

import com.google.gson.annotations.SerializedName

data class DormitoryResult(
    @SerializedName("dormitoryId") val dormitoryId: Int,
    @SerializedName("dormitoryName") val dormitoryName: String,
    @SerializedName("id") val id: Int
)

data class DormitoryResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: DormitoryResult
)

data class DormitoryRequest(
    @SerializedName("dormitoryId") var dormitoryId: Int
)