package com.example.geeksasaeng.Login.Retrofit

import com.example.geeksasaeng.Login.Retrofit.LoginResult

interface LoginView {
    fun onLoginSuccess(code : Int, result : LoginResult)
    fun onLoginFailure(code : Int, message : String)
}