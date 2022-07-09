package com.example.geeksasaeng.Signup

import com.example.geeksasaeng.Signup.Retrofit.SignUpSmsResult
import com.example.geeksasaeng.Signup.Retrofit.VerifySmsResult

interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure()
}

interface SignUpSmsView {
    fun onSignUpSmsSuccess(result: SignUpSmsResult)
    fun onSignUpSmsFailure(code:Int)
}

interface VerifySmsView {
    fun onVerifySmsSuccess(result: VerifySmsResult)
    fun onVerifySmsFailure(code:Int)
}