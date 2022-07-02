package com.example.geeksasaeng.Data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupRetrofitInterfaces {
    @POST("/members")
    fun signup(@Body user: Signup): Call<SignupResponse>
}