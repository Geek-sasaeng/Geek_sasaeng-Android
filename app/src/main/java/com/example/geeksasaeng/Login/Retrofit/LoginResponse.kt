package com.example.geeksasaeng.Login.Retrofit

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("dormitoryId") val dormitoryId: Int,
    @SerializedName("dormitoryName") val dormitoryName: String,
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("jwt") val jwt: String,
    @SerializedName("loginStatus") val loginStatus: String,
    @SerializedName("memberId") val memberId: Int,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("profileImgUrl") val profileImgUrl: String
)

data class LoginResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: LoginResult?
)

data class Login(
    @SerializedName("loginId") var loginId: String? = "",
    @SerializedName("password") var password: String? = "",
    @SerializedName("fcmToken") var fcmToken: String?
)

data class AutoLoginResult(
    @SerializedName("dormitoryId") val dormitoryId: Int,
    @SerializedName("dormitoryName") val dormitoryName: String,
    @SerializedName("email") val email: String,
    @SerializedName("loginId") val loginId: String,
    @SerializedName("loginStatus") val loginStatus: String,
    @SerializedName("memberLoginType") val memberLoginType: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("profileImgUrl") val profileImgUrl: String,
    @SerializedName("universityName") val universityName: String,
)

data class AutoLoginResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: AutoLoginResult?
)