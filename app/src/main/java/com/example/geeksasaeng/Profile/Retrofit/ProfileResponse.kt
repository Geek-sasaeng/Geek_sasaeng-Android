package com.example.geeksasaeng.Profile.Retrofit

import com.example.geeksasaeng.Signup.Retrofit.SignUpResult
import com.google.gson.annotations.SerializedName

data class ProfileAnnouncementResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ProfileAnnouncementResult?
)

data class ProfileAnnouncementResult(
    @SerializedName("announcementId") val announcementId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("imgUrl") val imgUrl: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("status") val status: String,

)

data class ProfileAnnouncementRequest (
    @SerializedName("announcementId") var announcementId: String
)