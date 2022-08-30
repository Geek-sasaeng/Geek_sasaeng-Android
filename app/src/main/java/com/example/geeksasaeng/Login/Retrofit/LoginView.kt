package com.example.geeksasaeng.Login.Retrofit

interface LoginView {
    fun onLoginSuccess(code : Int, result : LoginResult)
    fun onLoginFailure(message : String)
}
