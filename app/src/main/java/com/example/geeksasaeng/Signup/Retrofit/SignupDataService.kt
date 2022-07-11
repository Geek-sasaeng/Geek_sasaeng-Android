package com.example.geeksasaeng.Signup.Retrofit

import android.util.Log
import com.example.geeksasaeng.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Data.EmailCheck
import com.example.geeksasaeng.Data.Signup
import com.example.geeksasaeng.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupDataService() {

    lateinit var signup: Signup

    private lateinit var signUpView: SignUpView
    private lateinit var signUpEmailView: SignUpEmailView
    private lateinit var verifyEmailView: VerifyEmailView
    private lateinit var signUpSmsView: SignUpSmsView
    private lateinit var verifySmsView: VerifySmsView

    private val SignupDataService = retrofit.create(SignupRetrofitInterfaces::class.java)

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setSignUpEmailView() {
        this.signUpEmailView = signUpEmailView
    }

    fun setVerifyEmailView() {
        this.verifyEmailView = verifyEmailView
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

    // email 보내기
    fun signUpEmailSender(signupEmailRequest: SignUpEmailRequest) {
        SignupDataService.signupEmail(signupEmailRequest).enqueue(object : Callback<SignUpEmailResponse> {
            override fun onResponse(call: Call<SignUpEmailResponse>, response: Response<SignUpEmailResponse>) {
                Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : response.code = " + response.code())
                Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val emailResponse: SignUpEmailResponse = response.body()!!
                    Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : emailResponse.code = " + emailResponse.code)

                    when (emailResponse.code) {
                        2802 -> signUpEmailView.onSignUpEmailSuccess(emailResponse.code)
                        // 2803 : 유효하지 않은 인증번호 | 2804 : 이메일 인증 최대 10회 오류 | 2805 : 재시도
                        2803, 2804, 2805 -> signUpEmailView.onSignUpEmailFailure(emailResponse.code, emailResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<SignUpEmailResponse>, t: Throwable) {
                //실패처리
                Log.d("EMAIL-RESPONSE", "EmailDataService-onFailure : EmailFailed", t)
            }
        })
    }

    // email 인증확인
    fun verifyEmailSender(verifyEmailRequest: VerifyEmailRequest) {
        SignupDataService.verifyEmail(verifyEmailRequest).enqueue(object : Callback<VerifyEmailResponse> {
            override fun onResponse(call: Call<VerifyEmailResponse>, response: Response<VerifyEmailResponse>) {
                Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : response.code = " + response.code())
                Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val emailResponse: VerifyEmailResponse = response.body()!!
                    Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : emailResponse.code = " + emailResponse.code)

                    when (emailResponse.code) {
                        2800 -> verifyEmailView.onVerifyEmailSuccess(emailResponse.code)
                        // 2803 : 유효하지 않은 인증번호
                        2803 -> verifyEmailView.onVerifyEmailFailure(emailResponse.code, emailResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<VerifyEmailResponse>, t: Throwable) {
                //실패처리
                Log.d("EMAIL-RESPONSE", "EmailDataService-onFailure : EmailFailed", t)
            }
        })
    }
    
    // sms 보내기
    fun signUpSmsSender(recipientPhoneNumber: SignUpSmsRequest) {
        Log.d("sms-body", recipientPhoneNumber.toString())
        SignupDataService.signupSms(recipientPhoneNumber).enqueue(object : Callback<SignUpSmsResponse> {
            override fun onResponse(call: Call<SignUpSmsResponse>, response: Response<SignUpSmsResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("sms-response", response.toString())
                    Log.d("sms-resp", resp.toString())

                    when (resp.code) {
                        1001 -> {
                            signUpSmsView.onSignUpSmsSuccess(resp.message)
                        }
                        else -> signUpSmsView.onSignUpSmsFailure(resp.code)
                    }
                }
            }

            //통신자체가 안되면
            override fun onFailure(call: Call<SignUpSmsResponse>, t: Throwable) {
                /*Log.d("hi",t.toString())*/
                Log.d("SignUpSmsSender-RESP", "SignupDataService-onFailure : SignUpSmsFailed", t)
            }
        }
        )
    }

    // sms 인증확인
    fun verifySmsSender(verifySmsRequest: VerifySmsRequest) {
        SignupDataService.verifySms(verifySmsRequest).enqueue(object:
            Callback<VerifySmsResponse>{
            override fun onResponse(
                call: Call<VerifySmsResponse>,
                response: Response<VerifySmsResponse>
            ) {

                //TODO: 200이 OK던데 이게 성공이야? 1002:SMS인증에 성공했습니다. 이게 성공이야??
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("success", response.toString())

                    when (resp.code) {
                        1002 -> verifySmsView.onVerifySmsSuccess(resp.result!!)
                        else -> verifySmsView.onVerifySmsFailure(resp.code)
                    }
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