package com.example.geeksasaeng.Signup.Retrofit

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body

data class SignUpResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SignUpResult?
)

data class SignUpResult(
    @SerializedName("email") val email: String,
    @SerializedName("loginId") val loginId: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("phoneNumber") val phoneNumber: String?,
    @SerializedName("universityName") val universityName: String
)

data class SignUpRequest (
    @SerializedName("checkPassword") var checkPassword: String,
    @SerializedName("emailId") var email: Int?,
    @SerializedName("informationAgreeStatus") var informationAgreeStatus: String, //개인정보 동의
    @SerializedName("loginId") var loginId: String,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("password") var password: String,
    @SerializedName("phoneNumberId") var phoneNumberId: Int?,
    @SerializedName("universityName") var universityName: String,
)

//아이디 중복 체크
data class SignUpIdCheckResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SignUpIdCheckResult?
)

data class SignUpIdCheckResult(
    @SerializedName("result") val result: String
)

data class SignUpIdCheckRequest(
    @SerializedName("loginId") val loginId : String
)

//닉네임 중복 체크
data class SignUpNickCheckResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SignUpNickCheckResult?
)

data class SignUpNickCheckResult(
    @SerializedName("result") val result: String
)

data class SignUpNickCheckRequest(
    @SerializedName("nickName") val nickName : String
)

// email 보내기
data class SignUpEmailResponse(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String
)

data class SignUpEmailRequest (
    @SerializedName("email") var email: String? = "",
    @SerializedName("university") var university: String? = "",
    @SerializedName("uuid") var uuid: String? = ""
)

// email 인증
data class VerifyEmailResponse(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: VerifyEmailResult?
)

data class VerifyEmailResult(
    @SerializedName("emailId") val emailId: Int
)

data class VerifyEmailRequest(
    @SerializedName("email") val email : String,
    @SerializedName("key") val key : String
)

//sms 보내기
data class SignUpSmsResponse(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SignUpSmsResult?
)

data class SignUpSmsResult(
    @SerializedName("requestId") val requestId : String,
    @SerializedName("requestTime") val requestTime : String,
    @SerializedName("statusCode") val statusCode : String,
    @SerializedName("statusName") val statusName : String
)

data class SignUpSmsRequest(
    @SerializedName("recipientPhoneNumber") val recipientPhoneNumber : String,
    @SerializedName("uuid") val uuid : String,
)

//sms 인증
data class VerifySmsResponse(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: VerifySmsResult?
)

data class VerifySmsResult(
    @SerializedName("phoneNumberId") val phoneNumberId : Int?
)

data class VerifySmsRequest(
    @SerializedName("recipientPhoneNumber") val recipientPhoneNumber : String,
    @SerializedName("verifyRandomNumber") val verifyRandomNumber : String
)