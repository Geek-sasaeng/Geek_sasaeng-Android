package com.example.geeksasaeng.Signup.Email

import android.util.Log
import com.example.geeksasaeng.Data.EmailCheck
import com.example.geeksasaeng.Data.EmailSend
import com.example.geeksasaeng.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailDataService() {

    lateinit var emailSend: EmailSend
    lateinit var emailCheck: EmailCheck

    private lateinit var emailView: EmailView

    fun setEmailView(emailView: EmailView) {
        this.emailView = emailView
    }

    fun emailSend(emailSend: EmailSend) {
        val emailSendService = NetworkModule.getInstance()?.create(EmailRetrofitInterfaces::class.java)

        emailSendService?.sendEmail(emailSend)?.enqueue(object : Callback<EmailResponse> {
            override fun onResponse(call: Call<EmailResponse>, response: Response<EmailResponse>) {
                Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : response.code = " + response.code())
                Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val emailResponse: EmailResponse = response.body()!!
                    Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : emailResponse.code = " + emailResponse.code)

                    when (emailResponse.code) {
                        2802 -> emailView.onEmailSendSuccess(emailResponse.code)
                        // 2803 : 유효하지 않은 인증번호 | 2804 : 이메일 인증 최대 10회 오류 | 2805 : 재시도
                        2803, 2804, 2805 -> emailView.onEmailSendFailure(emailResponse.code, emailResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                //실패처리
                Log.d("EMAIL-RESPONSE", "EmailDataService-onFailure : EmailFailed", t)
            }
        })
    }

    fun emailCheck(emailCheck: EmailCheck) {
        val emailCheckService = NetworkModule.getInstance()?.create(EmailRetrofitInterfaces::class.java)

        emailCheckService?.checkEmail(emailCheck)?.enqueue(object : Callback<EmailResponse> {
            override fun onResponse(call: Call<EmailResponse>, response: Response<EmailResponse>) {
                Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : response.code = " + response.code())
                Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val emailResponse: EmailResponse = response.body()!!
                    Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : emailResponse.code = " + emailResponse.code)

                    when (emailResponse.code) {
                        2800 -> emailView.onEmailCheckSuccess(emailResponse.code)
                        // 2801 : 유효하지 않은 인증번호
                        2801 -> emailView.onEmailCheckFailure(emailResponse.code, emailResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                //실패처리
                Log.d("EMAIL-RESPONSE", "EmailDataService-onFailure : EmailFailed", t)
            }
        })
    }
}