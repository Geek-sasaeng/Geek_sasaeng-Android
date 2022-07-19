package com.example.geeksasaeng.Login.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginDataService() {

    lateinit var login: Login

    private lateinit var loginView: LoginView

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun login(user: Login) {
        val loginService = NetworkModule.getInstance()?.create(LoginRetrofitInterfaces::class.java)

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
                //실패처리
                Log.d("LOGIN-RESPONSE", "SignupDataService-onFailure : SignupFailed", t)
            }
        })
    }
}