package com.example.geeksasaeng.Signup.Email

import com.example.geeksasaeng.Data.EmailCheck
import com.example.geeksasaeng.Data.EmailSend
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EmailRetrofitInterfaces {
    @POST("/members/email")
    fun sendEmail(@Body emailSend: EmailSend): Call<EmailResponse>

    @POST("/members/email/check")
    fun checkEmail(@Body emailCheck: EmailCheck): Call<EmailResponse>
}