package com.example.geeksasaeng.Profile.Retrofit

import com.example.geeksasaeng.Signup.Retrofit.SignUpResult
import com.google.gson.annotations.SerializedName

data class ProfileRecentActivityResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<ProfileRecentActivityResult>?
)

data class ProfileRecentActivityResult(
    @SerializedName("id") val id: Int,
    @SerializedName("storeUrl") val storeUrl: String,
    @SerializedName("title") val title: String
)

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

data class ProfileMyAccountResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ProfileMyAccountResult
)

data class ProfileMyAccountResult(
    @SerializedName("dormitoryId") val dormitoryId: Int,
    @SerializedName("dormitoryName") val dormitoryName: Int,
    @SerializedName("emailAddress") val emailAddress: String,
    @SerializedName("emailId") val emailId: Int,
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("id") val id: Int,
    @SerializedName("informationAgreeStatus") val informationAgreeStatus: String,
    @SerializedName("loginId") val loginId: String,
    @SerializedName("memberLoginType") val memberLoginType: String,
    @SerializedName("loginStatus") val loginStatus: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("perDayReportingCount") val perDayReportingCount: Int,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("profileImgUrl") val profileImgUrl: String,
    @SerializedName("reportedCount") val reportedCount: Int,
    @SerializedName("universityId") val universityId: Int,
    @SerializedName("universityName") val universityName: String,
)