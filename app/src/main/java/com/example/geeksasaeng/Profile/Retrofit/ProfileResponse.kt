package com.example.geeksasaeng.Profile.Retrofit

import com.example.geeksasaeng.Signup.Retrofit.SignUpResult
import com.google.gson.annotations.SerializedName

data class ProfileMyOngoingActivityResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ArrayList<ProfileMyOngoingActivityResult>?
)

data class ProfileMyOngoingActivityResult(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("createdAt") val createdAt: String
)

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
    @SerializedName("result") val result: ProfileAnnouncementDetailResult?
)

data class ProfileAnnouncementDetailResult(
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

data class ProfileMyInfoResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ProfileMyInfoResult
)

data class ProfileMyInfoResult(
    @SerializedName("dormitoryId") val dormitoryId: Int,
    @SerializedName("dormitoryName") val dormitoryName: String,
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

// 회원 탈퇴
data class ProfileWithdrawalResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)

data class ProfileWithdrawalRequest(
    @SerializedName("checkPassword") val checkPassword: String,
    @SerializedName("password") val password: String
)