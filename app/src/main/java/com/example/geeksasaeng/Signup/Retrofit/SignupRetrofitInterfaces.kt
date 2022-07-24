package com.example.geeksasaeng.Signup.Retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignupRetrofitInterfaces {
    // 일반 회원가입
    @POST("/members")
    fun signup(@Body user: SignUpRequest): Call<SignUpResponse>

    // 네이버 회원가입
    @POST("/members/social")
    fun signupSocial(@Body user: SignUpRequest): Call<SignUpResponse>

    // <이메일 인증>
    // 이메일 전송
    @POST("/email")
    fun signupEmail(@Body signupEmailRequest: SignUpEmailRequest): Call<SignUpEmailResponse>

    // 이메일 인증 확인
    @POST("/email/check")
    fun verifyEmail(@Body verifyEmailRequest: VerifyEmailRequest): Call<VerifyEmailResponse>

    // 아이디 중복 확인
    @POST("/members/id-duplicated")
    fun signupIdCheck(
        @Body loginId : SignUpIdCheckRequest
    ): Call<SignUpIdCheckResponse>

    // 닉네임 중복 확인
    @POST("/members/nickname-duplicated")
    fun signupNickCheck(@Body nickName : SignUpNickCheckRequest): Call<SignUpNickCheckResponse>

    // <문자 인증>
    // sms보내기
    @POST("/sms")
    fun signupSms(@Body recipientPhoneNumber: SignUpSmsRequest): Call<SignUpSmsResponse>

    // sms인증확인
    @POST("/sms/verification")
    fun verifySms(@Body verifySmsRequest: VerifySmsRequest): Call<VerifySmsResponse>
}