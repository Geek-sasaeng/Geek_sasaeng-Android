package com.example.geeksasaeng.Login.Retrofit


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRetrofitInterfaces {
    @POST("/login")
    fun login(@Body user: Login): Call<LoginResponse>

    // 자동로그인
    @POST("/login/auto")
    fun autoLogin(
        ): Call<AutoLoginResponse>

    // 네이버 로그인, accessToken 전달
    @POST("/login/social")
    fun naverLogin(@Body accessToken: SocialLogin): Call<SocialLoginResponse>
}