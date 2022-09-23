package com.example.geeksasaeng.Login.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginDataService() {

    lateinit var login: Login

    private lateinit var loginView: LoginView
    private lateinit var autoLoginView: AutoLoginView

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun setAutoLoginView(autoLoginView: AutoLoginView){
        this.autoLoginView = autoLoginView
    }

    fun login(user: Login) {
        val loginService = NetworkModule.getInstance()?.create(LoginRetrofitInterfaces::class.java)

        loginService?.login(user)?.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LOGIN-RESPONSE", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val loginResponse: LoginResponse = response.body()!!

                    Log.d("LOGIN-RESP", loginResponse.toString())
                    when (loginResponse.code) {
                        1000 -> loginView.onLoginSuccess(loginResponse.code, loginResponse.result!!)
                        4000 -> Log.d("LOGIN", "서버 오류")
                        else -> loginView.onLoginFailure(loginResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                //실패처리
                Log.d("LOGIN-RESPONSE", "SignupDataService-onFailure : SignupFailed"+t.toString(), t)
            }
        })
    }
    fun autoLogin() {
        val loginService = NetworkModule.getInstance()?.create(LoginRetrofitInterfaces::class.java)

        loginService?.autoLogin()?.enqueue(object : Callback<AutoLoginResponse> {
            override fun onResponse(call: Call<AutoLoginResponse>, response: Response<AutoLoginResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val autoLoginResponse: AutoLoginResponse = response.body()!!
                    when (autoLoginResponse.code) {
                        1000 -> autoLoginView.onAutoLoginSuccess(autoLoginResponse.code, autoLoginResponse.result!!)
                        4000 -> Log.d("LOGIN", "서버 오류")
                        else -> autoLoginView.onAutoLoginFailure(autoLoginResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<AutoLoginResponse>, t: Throwable) {
                //실패처리
                Log.d("LOGIN-RESPONSE", "SignupDataService-onFailure : SignupFailed-자동로그인", t)
            }
        })
    }
}