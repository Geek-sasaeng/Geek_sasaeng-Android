package com.example.geeksasaeng

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.Login.Retrofit.Login
import com.example.geeksasaeng.Login.Retrofit.LoginDataService
import com.example.geeksasaeng.Login.Retrofit.LoginResult
import com.example.geeksasaeng.Login.Retrofit.LoginView
import com.example.geeksasaeng.Utils.*
import com.example.geeksasaeng.databinding.ActivitySplashBinding

class SplashActivity: BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate), LoginView {

    var jwt: String? = null
    var loginId: String? = null
    var password: String? = null

    override fun initAfterBinding() {
        // Splash Animation 적용
        binding.splashAnimView.imageAssetsFolder = "images"
        binding.splashAnimView.playAnimation()

        val handler = Handler(Looper.getMainLooper())

        jwt = getJwt()
        loginId = getLoginId()
        password = getPassword()

        Log.d("Splash-Response", getJwt() + " "  + getLoginId() + " " + getPassword())

        handler.postDelayed({
            if (loginId != null && password != null) {
                login()
            }
            else if(jwt != null) { // 네이버 자동 로그인 체크 한 경우
                changeMainActivity()
            }else{
                removeAutoLogin()
                changeActivity(LoginActivity::class.java)
                finish()
            }
        }, 1500)
    }

    private fun login() {
        val loginDataService = LoginDataService()
        loginDataService.setLoginView(this)
        loginDataService.login(Login(loginId, password))
    }
    private fun changeMainActivity() {
        changeActivity(MainActivity::class.java)
        finish()
    }

    override fun onLoginSuccess(code : Int , result: LoginResult) {
        // 자동 로그인 수정 필요
        changeMainActivity()
    }

    override fun onLoginFailure(message: String) {
        // 2011 : 비밀번호가 틀립니다
        // 2012 : 탈퇴한 회원
        // 2400 : 존재하지 않는 아이디
        Log.d("Splash-Response", "fail")
        removeAutoLogin()
        changeActivity(LoginActivity::class.java)
    }

}