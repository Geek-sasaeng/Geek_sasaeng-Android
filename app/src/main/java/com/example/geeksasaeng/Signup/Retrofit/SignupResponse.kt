package com.example.geeksasaeng.Signup.Retrofit

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body


data class SignUpResponse(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SignUpResult?
)

data class SignUpResult(
    @SerializedName("email") val email: String,
    @SerializedName("loginId") val loginId: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("universityName") val universityName: String
)

// email 보내기
data class SignUpEmailResponse(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: String?
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
    @SerializedName("result") val result: String?
)

data class VerifyEmailRequest(
    @SerializedName("email") val email : String,
    @SerializedName("key") val key : Int
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
    @SerializedName("statusName") val statusName : String
)

data class VerifySmsRequest(
    @SerializedName("recipientPhoneNumber") val recipientPhoneNumber : String,
    @SerializedName("verifyRandomNumber") val verifyRandomNumber : String
)

