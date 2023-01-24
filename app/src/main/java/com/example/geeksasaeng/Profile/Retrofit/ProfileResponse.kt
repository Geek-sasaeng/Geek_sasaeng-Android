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
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("createdAt") val createdAt: String,
    val viewType: Int = 0
)

// 진행 중인 활동 활동 아이템 및 날짜 아이템 구분
const val myOngoingActivity = 1
const val myOngoingActivityDate = 2

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

// 내 정보 보기
data class ProfileMyInfoResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ProfileMyInfoResult
)

data class ProfileMyInfoResult(
    @SerializedName("createdAt") val createdAt: String,
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

// 진행했던 활동 조회
data class ProfileMyPreActivityResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ProfileMyPreActivityResult
)

data class ProfileMyPreActivityResult(
    @SerializedName("endedDeliveryPartiesVoList") val endedDeliveryPartiesVoList: ArrayList<EndedDeliveryPartiesVoList>,
    @SerializedName("finalPage") val finalPage: Boolean
)

data class EndedDeliveryPartiesVoList(
    @SerializedName("foodCategory") val foodCategory: String,
    @SerializedName("id") val id: Int,
    @SerializedName("maxMatching") val maxMatching: Int,
    @SerializedName("title") val title: String,
    @SerializedName("updatedAt") val updatedAt: String
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

// 회원 정보 수정
data class ProfileMemberInfoModifyResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ProfileMemberInfoModifyResult
)

data class ProfileMemberInfoModifyResult(
    @SerializedName("dormitoryId") val dormitoryId: Int,
    @SerializedName("dormitoryName") val dormitoryName: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImgUrl") val profileImgUrl: String
)

// 비밀번호 일치 확인
data class ProfilePasswordCheckingRequest(
    @SerializedName("password") val password: String
)

data class ProfilePasswordCheckingResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)

// 비밀번호 수정
data class ProfilePasswordChangeRequest(
    @SerializedName("checkNewPassword") val checkNewPassword: String,
    @SerializedName("newPassword") val newPassword: String
)

data class ProfilePasswordChangeResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)
