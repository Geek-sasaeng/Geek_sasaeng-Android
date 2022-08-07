package com.example.geeksasaeng

import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.VideoView
import com.airbnb.lottie.RenderMode
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.Login.Retrofit.Login
import com.example.geeksasaeng.Login.Retrofit.LoginDataService
import com.example.geeksasaeng.Login.Retrofit.LoginResult
import com.example.geeksasaeng.Login.Retrofit.LoginView
import com.example.geeksasaeng.Utils.*
import com.example.geeksasaeng.databinding.ActivitySplashBinding
import java.lang.Exception

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate),
    LoginView {
    var jwt: String? = null
    var loginId: String? = null
    var password: String? = null

    override fun initAfterBinding() {
//      Splash Animation 적용
        val animationView = binding.animationView
        animationView.setRenderMode(RenderMode.AUTOMATIC)
        animationView.enableMergePathsForKitKatAndAbove(true)
        animationView.imageAssetsFolder = "images"
        animationView.setAnimation("data.json");
        animationView.playAnimation()

        // 화면 전환
        jump()
    }
    private fun jump() {
        jwt = getJwt()
        loginId = getLoginId()
        password = getPassword()

        Log.d("Splash-Response", getJwt() + " " + getLoginId() + " " + getPassword())

        if (loginId != null && password != null) {
            login()
        } else if (jwt != null) { // 네이버 자동 로그인 체크 한 경우
            changeMainActivity()
        } else {
            removeAutoLogin()
            changeActivity(LoginActivity::class.java)
            finish()
        }
    }
    private fun login() {
        val loginDataService = LoginDataService()
        loginDataService.setLoginView(this)
        loginDataService.login(Login(loginId, password))
    }

    private fun changeMainActivity() {
        changeActivity(LoginActivity::class.java) // 디버깅용
        //changeActivity(MainActivity::class.java)
        finish()
    }

    override fun onLoginSuccess(code: Int, result: LoginResult) {
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