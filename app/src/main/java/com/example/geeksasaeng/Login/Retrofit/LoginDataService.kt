package com.example.geeksasaeng.Login.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class LoginDataService() {

    lateinit var login: Login

    private lateinit var loginView: LoginView
    private lateinit var autoLoginView: AutoLoginView
    private lateinit var socialLoginView: SocialLoginView

    private var loginService = NetworkModule.getInstance()?.create(LoginRetrofitInterfaces::class.java)

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }
    fun setAutoLoginView(autoLoginView: AutoLoginView){
        this.autoLoginView = autoLoginView
    }
    fun setSocialLoginView(socialLoginView: SocialLoginView) {
        this.socialLoginView = socialLoginView
    }

    fun login(user: Login) {
        loginService?.login(user)?.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val loginResponse: LoginResponse = response.body()!!
                    when (loginResponse.code) {
                        1000 -> loginView.onLoginSuccess(loginResponse.code, loginResponse.result!!)
                        4000 -> Log.d("LOGIN", "서버 오류")
                        else -> loginView.onLoginFailure(loginResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("LOGIN-RESPONSE", "SignupDataService-onFailure : SignupFailed"+t.toString(), t)
            }
        })
    }
    fun autoLogin() {
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
                Log.d("LOGIN-RESPONSE", "SignupDataService-onFailure : SignupFailed-자동로그인", t)
            }
        })
    }

    fun socialLogin(accessToken: SocialLogin){
        loginService?.naverLogin(accessToken)?.enqueue(object: Callback<SocialLoginResponse>{
            override fun onResponse(call: Call<SocialLoginResponse>, response: Response<SocialLoginResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    Log.d("API-TEST", "socialLogin code = ${response.body()!!.code}")
                    Log.d("API-TEST", "socialLogin code = ${response.body()!!.message}")
                    val socialLoginResponse: SocialLoginResponse = response.body()!!
                    when (socialLoginResponse.code) {
                        1000 -> socialLoginView.onSocialLoginSuccess(socialLoginResponse.code, socialLoginResponse.result!!) // 로그인
                        2807 -> socialLoginView.onSocialLoginToRegister(socialLoginResponse.message, accessToken.accessToken)
                        4000 -> Log.d("LOGIN", "서버 오류")
                        else -> socialLoginView.onLoginFailure(socialLoginResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<SocialLoginResponse>, t: Throwable) {
                Log.d("LOGIN-RESPONSE", "SignupDataService-onFailure : SignupFailed", t)
            }
        })
    }
}