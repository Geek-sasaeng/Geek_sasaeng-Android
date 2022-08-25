package com.example.geeksasaeng.Login.Retrofit

import com.google.gson.annotations.SerializedName

/*
    소셜 로그인 (네이버 로그인)
 */
data class SocialLogin(
    @SerializedName("accessToken") var accessToken: String,
    @SerializedName("fcmToken") var fcmToken: String
)

data class SocialLoginResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SocialLoginResult?
)

data class SocialLoginResult(
    @SerializedName("dormitoryId") val dormitoryId: Int,
    @SerializedName("dormitoryName") val dormitoryName: String,
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("jwt") val jwt: String,
    @SerializedName("loginStatus") val loginStatus: String,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("profileImgUrl") val profileImgUrl: String
)
