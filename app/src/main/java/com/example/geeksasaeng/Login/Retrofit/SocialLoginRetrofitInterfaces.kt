package com.example.geeksasaeng.Login.Retrofit

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SocialLoginRetrofitInterfaces {
    // 네이버 로그인, accessToken 전달
    @POST("/login/social")
    fun naverLogin(@Body accessToken: SocialLogin): Call<SocialLoginResponse>
}