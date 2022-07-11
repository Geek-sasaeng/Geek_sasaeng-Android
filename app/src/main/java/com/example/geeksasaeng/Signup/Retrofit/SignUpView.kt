package com.example.geeksasaeng.Signup.Retrofit

import com.example.geeksasaeng.Signup.Retrofit.SignUpSmsResult
import com.example.geeksasaeng.Signup.Retrofit.VerifySmsResult

interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure()
}

interface SignUpEmailView {
    fun onSignUpEmailSuccess(message: String)
    fun onSignUpEmailFailure(code : Int, message : String)
}

interface VerifyEmailView {
    fun onVerifyEmailSuccess(message: String)
    fun onVerifyEmailFailure(code: Int, message: String)
}

interface SignUpSmsView {
    fun onSignUpSmsSuccess(message: String)
    fun onSignUpSmsFailure(code:Int)
}

interface VerifySmsView {
    fun onVerifySmsSuccess(result: VerifySmsResult)
    fun onVerifySmsFailure(code:Int)
}