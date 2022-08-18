package com.example.geeksasaeng.Profile.Retrofit

import com.example.geeksasaeng.Signup.Retrofit.SignUpResult
import com.google.gson.annotations.SerializedName

//공지사항 전체조회
data class ProfileAnnouncementResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<ProfileAnnouncementResult>
)

data class ProfileAnnouncementResult(
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("id") val id: Int,
    @SerializedName("imgUrl") val imgUrl: String?,
    @SerializedName("title") val title: String,
    @SerializedName("updatedAt") val updatedAt: String,
    //@SerializedName("status") val status: String //TODO:이게 있어야할 것 같은데
)



//공지사항 상세조회
data class ProfileAnnouncementDetailResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ProfileAnnouncementDeatilResult?
)

data class ProfileAnnouncementDeatilResult(
    @SerializedName("announcementId") val announcementId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("imgUrl") val imgUrl: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("status") val status: String,

)

data class ProfileAnnouncementDetailRequest (
    @SerializedName("announcementId") var announcementId: Int
)