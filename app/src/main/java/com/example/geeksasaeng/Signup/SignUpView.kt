package com.example.geeksasaeng.Signup

import com.example.geeksasaeng.Signup.Retrofit.SignUpSmsResult
import com.example.geeksasaeng.Signup.Retrofit.VerifySmsResult

interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure()
}

//아이디 중복확인
interface SignUpIdCheckView{
    fun onSignUpIdCheckSuccess(message: String)
    fun onSignUpIdCheckFailure(code:Int)
}


//SMS문자인증 보내기
interface SignUpSmsView {
    fun onSignUpSmsSuccess(message: String)
    fun onSignUpSmsFailure(code:Int)
}

//SMS문자인증 확인
interface VerifySmsView {
    fun onVerifySmsSuccess(result: VerifySmsResult)
    fun onVerifySmsFailure(code:Int)
}