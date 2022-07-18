package com.example.geeksasaeng.Signup.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupDataService() {

    //뷰 객체 생성
    private lateinit var signUpView: SignUpView
    private lateinit var signUpIdCheckView : SignUpIdCheckView
    private lateinit var signUpNickCheckView: SignUpNickCheckView
    private lateinit var signUpEmailView: SignUpEmailView
    private lateinit var verifyEmailView: VerifyEmailView
    private lateinit var signUpSmsView: SignUpSmsView
    private lateinit var verifySmsView: VerifySmsView

    private val SignupDataService = retrofit.create(SignupRetrofitInterfaces::class.java)

    //setView
    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setSignUpEmailView(signUpEmailView: SignUpEmailView) {
        this.signUpEmailView = signUpEmailView
    }

    fun setVerifyEmailView(verifyEmailView: VerifyEmailView) {
        this.verifyEmailView = verifyEmailView
    }

    fun setSignUpIdCheckView(signUpIdCheckView: SignUpIdCheckView){
        this.signUpIdCheckView = signUpIdCheckView
    }

    fun setSignUpNickCheckView(signUpNickCheckView: SignUpNickCheckView){
        this.signUpNickCheckView = signUpNickCheckView
    }

    fun setSignUpSmsView(signUpSmsView: SignUpSmsView){
        this.signUpSmsView = signUpSmsView
    }

    fun setVerifySmsView(verifySmsView: VerifySmsView){
        this.verifySmsView = verifySmsView
    }

    //회원가입
    fun signUpSender(user: SignUpRequest) {
        SignupDataService.signup(user).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {

                if (response.isSuccessful && response.code() == 200) {
                    val signUpResponse: SignUpResponse = response.body()!!

                    when (signUpResponse.code) {
                        1000 -> signUpView.onSignUpSuccess() //회원가입 성공
                        else -> signUpView.onSignUpFailure(signUpResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.d("SIGNUP-RESPONSE", "SignupDataService-onFailure : SignupFailed", t)
            }
        })
    }

    // 네이버 회원가입
    fun signUpSocial(user: SignUpRequest) {
        SignupDataService?.signupSocial(user)?.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                Log.d("SIGNUP-RESPONSE", "SignupDataService-onResponse : response.code = " + response.code())
                Log.d("SIGNUP-RESPONSE", "SignupDataService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val signUpResponse: SignUpResponse = response.body()!!

                    when (signUpResponse.code) {
                        1000 -> signUpView.onSignUpSuccess() //회원가입 성공
                        else -> signUpView.onSignUpFailure(signUpResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.d("SIGNUP-RESPONSE", "SignupDataService-onFailure : SignupFailed", t)
            }
        })
    }

    // email 보내기
    fun signUpEmailSender(signupEmailRequest: SignUpEmailRequest) {
        SignupDataService.signupEmail(signupEmailRequest).enqueue(object : Callback<SignUpEmailResponse> {
            override fun onResponse(call: Call<SignUpEmailResponse>, response: Response<SignUpEmailResponse>) {
                Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : response.code = " + response.code())

                if (response.isSuccessful && response.code() == 200) {
                    val emailResponse: SignUpEmailResponse = response.body()!!
                    Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : emailResponse.code = " + emailResponse.code)

                    when (emailResponse.code) {
                        1802 -> signUpEmailView.onSignUpEmailSuccess(emailResponse.message)
                        else -> signUpEmailView.onSignUpEmailFailure(emailResponse.code, emailResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<SignUpEmailResponse>, t: Throwable) {
                Log.d("EMAIL-RESPONSE", "EmailDataService-onFailure : EmailFailed", t)
            }
        })
    }

    // email 인증확인
    fun verifyEmailSender(verifyEmailRequest: VerifyEmailRequest) {
        SignupDataService.verifyEmail(verifyEmailRequest).enqueue(object : Callback<VerifyEmailResponse> {
            override fun onResponse(call: Call<VerifyEmailResponse>, response: Response<VerifyEmailResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val emailResponse: VerifyEmailResponse = response.body()!!
                    Log.d("EMAIL-RESPONSE", "EmailDataService-onResponse : emailResponse.code = " + emailResponse.code)

                    when (emailResponse.code) {
                        1801 -> verifyEmailView.onVerifyEmailSuccess(emailResponse.message)
                        else -> verifyEmailView.onVerifyEmailFailure(emailResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<VerifyEmailResponse>, t: Throwable) {
                Log.d("EMAIL-RESPONSE", "EmailDataService-onFailure : EmailFailed", t)
            }
        })
    }

    // 아이디 중복확인
    fun signUpIdCheckSender(loginId: SignUpIdCheckRequest){
        SignupDataService.signupIdCheck(loginId).enqueue(object: Callback<SignUpIdCheckResponse>{
            override fun onResponse(call: Call<SignUpIdCheckResponse>, response: Response<SignUpIdCheckResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp: SignUpIdCheckResponse = response.body()!!

                    when (resp.code) {
                        1601 -> signUpIdCheckView.onSignUpIdCheckSuccess(resp.message)
                        4000 -> Log.d("ID", "서버 오류")
                        else -> signUpIdCheckView.onSignUpIdCheckFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<SignUpIdCheckResponse>, t: Throwable) {
                Log.d("SignUpIdCheckSender", "SignupDataService-onFailure : SignUpIdCheckSmsFailed", t)
            }
        })
    }

    // 닉네임 중복확인
    fun signUpNickCheckSender(signUpNickRequest: SignUpNickCheckRequest){
        SignupDataService.signupNickCheck(signUpNickRequest).enqueue(object: Callback<SignUpNickCheckResponse>{
            override fun onResponse(call: Call<SignUpNickCheckResponse>, response: Response<SignUpNickCheckResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp: SignUpNickCheckResponse = response.body()!!

                    when (resp.code) {
                        1202-> signUpNickCheckView.onSignUpNickCheckSuccess(resp.message)
                        4000 -> Log.d("NICKNAME", "서버 오류")
                        else -> signUpNickCheckView.onSignUpNickCheckFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<SignUpNickCheckResponse>, t: Throwable) {
                TODO("Not yet implemented")
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

                    when (resp.code) {
                        1001 -> signUpSmsView.onSignUpSmsSuccess(resp.message)
                        else -> signUpSmsView.onSignUpSmsFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<SignUpSmsResponse>, t: Throwable) {
                Log.d("SignUpSmsSender-RESP", "SignupDataService-onFailure : SignUpSmsFailed", t)
            }
        })
    }

    // sms 인증확인
    fun verifySmsSender(verifySmsRequest: VerifySmsRequest) {
        SignupDataService.verifySms(verifySmsRequest).enqueue(object: Callback<VerifySmsResponse>{
            override fun onResponse(call: Call<VerifySmsResponse>, response: Response<VerifySmsResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!

                    when (resp.code) {
                        1002 -> verifySmsView.onVerifySmsSuccess(resp.result!!)
                        else -> verifySmsView.onVerifySmsFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<VerifySmsResponse>, t: Throwable) {
                Log.d("SignUpSmsSender-RESP", "SignupDataService-onFailure : SignUpSmsFailed", t)
            }
        })
    }
}