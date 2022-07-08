package com.example.geeksasaeng.Signup.Retrofit

import com.example.geeksasaeng.Data.Signup
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupRetrofitInterfaces {
    @POST("/members")
    fun signup(@Body user: Signup): Call<SignupResponse>
}