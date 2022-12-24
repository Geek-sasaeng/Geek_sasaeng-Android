package com.example.geeksasaeng.Login.Retrofit

interface LoginView {
    fun onLoginSuccess(code : Int, result : LoginResult)
    fun onLoginFailure(message : String)
}

interface SocialLoginView {
    fun onSocialLoginSuccess(code : Int, result : SocialLoginResult)
    fun onSocialLoginToRegister(message: String, accessToken: String)
}