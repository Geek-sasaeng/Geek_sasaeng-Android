package com.example.geeksasaeng.Login.Retrofit;
import android.util.Log
import com.example.geeksasaeng.Utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SocialLoginDataService {
    private lateinit var socialLoginView: SocialLoginView

    fun setSocialLoginView(socialLoginView: SocialLoginView) {
        this.socialLoginView = socialLoginView
    }

    fun socialLogin(accessToken: SocialLogin){
        val socialLoginService = NetworkModule.getInstance()?.create(SocialLoginRetrofitInterfaces::class.java)
        socialLoginService?.naverLogin(accessToken)?.enqueue(object: Callback<SocialLoginResponse>{
            override fun onResponse(call: Call<SocialLoginResponse>, response: Response<SocialLoginResponse>) {
                if (response.isSuccessful && response.code() == 200) {
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
                //실패처리
                Log.d("LOGIN-RESPONSE", "SignupDataService-onFailure : SignupFailed", t)
            }
        })
    }
}
