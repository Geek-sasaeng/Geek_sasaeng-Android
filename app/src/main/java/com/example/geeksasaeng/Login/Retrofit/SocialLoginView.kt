package com.example.geeksasaeng.Login.Retrofit

interface SocialLoginView {
    // 로그인 성공
    fun onSocialLoginSuccess(code : Int, result : SocialLoginResult)
    // 회원가입 창으로 이동
    fun onSocialLoginToRegister(message: String, accessToken: String)
    fun onLoginFailure(message : String)
}