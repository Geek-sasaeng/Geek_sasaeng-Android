package com.example.geeksasaeng.Signup.Retrofit

import android.util.Log
import com.example.geeksasaeng.Data.Signup
import com.example.geeksasaeng.Signup.SignUpView
import com.example.geeksasaeng.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupDataService() {

    lateinit var signup: Signup

    private lateinit var signUpView: SignUpView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun signUp(user: Signup) {
        val signUpService = getRetrofit().create(SignupRetrofitInterfaces::class.java)

        signUpService.signup(user).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                // Log.d("SIGNUP-RESPONSE", "SignupDataService-onResponse : response.code = " + response.code())
                // Log.d("SIGNUP-RESPONSE", "SignupDataService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val signUpResponse: SignupResponse = response.body()!!

                    when (signUpResponse.code) {
                        1000 -> signUpView.onSignUpSuccess()
                        else -> {
                            signUpView.onSignUpFailure()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                //실패처리
                Log.d("SIGNUP-RESPONSE", "SignupDataService-onFailure : SignupFailed", t)
            }
        })
    }
}