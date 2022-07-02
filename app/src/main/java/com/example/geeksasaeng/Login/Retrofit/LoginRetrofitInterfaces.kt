package com.example.geeksasaeng.Login.Retrofit

import com.example.geeksasaeng.Data.Login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRetrofitInterfaces {
    @POST("/login")
    fun login(@Body user: Login): Call<LoginResponse>
}