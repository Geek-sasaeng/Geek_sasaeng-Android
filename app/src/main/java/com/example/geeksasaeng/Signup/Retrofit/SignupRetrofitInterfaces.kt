package com.example.geeksasaeng.Signup.Retrofit

import com.example.geeksasaeng.Data.EmailCheck
import com.example.geeksasaeng.Data.Signup
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupRetrofitInterfaces {
    @POST("/members")
    fun signup(@Body user: Signup): Call<SignUpResponse>

    // <이메일 인증>
    // 이메일 전송
    @POST("/members/email")
    fun signupEmail(@Body signupEmailRequest: SignUpEmailRequest): Call<SignUpEmailRequest>

    // 이메일 인증 확인
    @POST("/members/email/check")
    fun verifyEmail(@Body verifyEmailRequest: VerifyEmailRequest): Call<VerifyEmailRequest>

    //<문자 인증>
    //sms보내기
    @POST("/sms")
    fun signupSms(@Body recipientPhoneNumber: SignUpSmsRequest): Call<SignUpSmsResponse>

    //sms인증확인
    @POST("/sms/verification")
    fun verifySms(@Body verifySmsRequest: VerifySmsRequest): Call<VerifySmsResponse>

}