package com.example.geeksasaeng.Login.Retrofit

interface AutoLoginView {
    fun onAutoLoginSuccess(code: Int, result: AutoLoginResult)
    fun onAutoLoginFailure(message : String)
}