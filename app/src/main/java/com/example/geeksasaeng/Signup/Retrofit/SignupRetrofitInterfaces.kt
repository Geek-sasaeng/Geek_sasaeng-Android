package com.example.geeksasaeng.Signup.Retrofit

import com.example.geeksasaeng.Data.Signup
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupRetrofitInterfaces {
    @POST("/members")
    fun signup(@Body user: Signup): Call<SignUpResponse>

    //<아이디 중복 확인>
    @POST("/members/id-duplicated")
    fun signupIdCheck(
        @Body loginId : SignUpIdCheckRequest
    ): Call<SignUpIdCheckResponse>

    //<닉네임 중복 확인>
    @POST("/members/nickname-duplicated")
    fun signupNickCheck(
        @Body nickName : SignUpNickCheckRequest
    ): Call<SignUpNickCheckResponse>

    //<문자 인증>
    //sms보내기
    @POST("/sms")
    fun signupSms(
        @Body recipientPhoneNumber: SignUpSmsRequest
    ): Call<SignUpSmsResponse>

    //sms인증확인
    @POST("/sms/verification")
    fun verifySms(
        @Body verifySmsRequest: VerifySmsRequest
    ): Call<VerifySmsResponse>

}