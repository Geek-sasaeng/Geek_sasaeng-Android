package com.example.geeksasaeng.Signup.Retrofit

import android.util.Log
import com.example.geeksasaeng.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Data.Signup
import com.example.geeksasaeng.NetworkModule
import com.example.geeksasaeng.Signup.SignUpSmsView
import com.example.geeksasaeng.Signup.SignUpView
import com.example.geeksasaeng.Signup.VerifySmsView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.sign

class SignupDataService() {

    lateinit var signup: Signup

    private lateinit var signUpView: SignUpView
    private lateinit var signUpSmsView: SignUpSmsView
    private lateinit var verifySmsView: VerifySmsView

    private val SignupDataService = retrofit.create(SignupRetrofitInterfaces::class.java)

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setSignUpSmsView(signUpSmsView: SignUpSmsView){
        this.signUpSmsView = signUpSmsView
    }

    fun setVerifySmsView(verifySmsView: VerifySmsView){
        this.verifySmsView = verifySmsView
    }

    fun signUp(user: Signup) {
        val signUpService = NetworkModule?.getInstance()?.create(SignupRetrofitInterfaces::class.java)

        signUpService?.signup(user)?.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                Log.d("SIGNUP-RESPONSE", "SignupDataService-onResponse : response.code = " + response.code())
                Log.d("SIGNUP-RESPONSE", "SignupDataService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val signUpResponse: SignUpResponse = response.body()!!

                    when (signUpResponse.code) {
                        1000 -> signUpView.onSignUpSuccess()
                        else -> {
                            signUpView.onSignUpFailure()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                //실패처리
                Log.d("SIGNUP-RESPONSE", "SignupDataService-onFailure : SignupFailed", t)
            }
        })
    }

    // sms 보내기
    fun signUpSmsSender(recipientPhoneNumber: SignUpSmsRequest) {

        SignupDataService.signupSms(recipientPhoneNumber).enqueue(object:
            Callback<SignUpSmsResponse>{
            override fun onResponse(
                call: Call<SignUpSmsResponse>,
                response: Response<SignUpSmsResponse>
            ) {
                //if (response.isSuccessful && response.code() == 200)TODO:이거 왜 필요한지
                val resp = response.body()!!
                Log.d("success",response.toString())

                when(resp.code) {
                    200 -> signUpSmsView.onSignUpSmsSuccess(resp.result!!)
                    else -> signUpSmsView.onSignUpSmsFailure(resp.code)
                }
            }

            //통신자체가 안되면
            override fun onFailure(call: Call<SignUpSmsResponse>, t: Throwable) {
                /*Log.d("hi",t.toString())*/
            }
        }
        )
    }

    // sms 인증확인
    fun VerifySmsSender(verifySmsRequest: VerifySmsRequest) {

        SignupDataService.verifySms(verifySmsRequest).enqueue(object:
            Callback<VerifySmsResponse>{
            override fun onResponse(
                call: Call<VerifySmsResponse>,
                response: Response<VerifySmsResponse>
            ) {
                val resp = response.body()!!
                Log.d("success",response.toString())

                when(resp.code) {
                    1002 -> verifySmsView.onVerifySmsSuccess(resp.result!!) //TODO: 200이 OK던데 이게 성공이야? 1002:SMS인증에 성공했습니다. 이게 성공이야??
                    else -> verifySmsView.onVerifySmsFailure(resp.code)
                }
            }

            //통신자체가 안되면
            override fun onFailure(call: Call<VerifySmsResponse>, t: Throwable) {
                /*Log.d("hi",t.toString())*/
            }
        }
        )
    }
}