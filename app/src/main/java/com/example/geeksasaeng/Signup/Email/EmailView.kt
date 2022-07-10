package com.example.geeksasaeng.Signup.Email

import com.example.geeksasaeng.Login.Retrofit.LoginResult

interface EmailView {
    fun onEmailSendSuccess(code : Int)
    fun onEmailSendFailure(code : Int, message : String)

    fun onEmailCheckSuccess(code : Int)
    fun onEmailCheckFailure(code : Int, message : String)
}