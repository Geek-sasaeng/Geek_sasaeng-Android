package com.example.geeksasaeng

import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.VideoView
import com.airbnb.lottie.RenderMode
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.Login.Retrofit.*
import com.example.geeksasaeng.Utils.*
import com.example.geeksasaeng.databinding.ActivitySplashBinding
import java.lang.Exception

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate), AutoLoginView {
    private lateinit var loginService: LoginDataService
    var jwt: String? = null
    var loginId: String? = null
    var password: String? = null

    override fun initAfterBinding() {
//      Splash Animation 적용
        val animationView = binding.animationView
        animationView.setRenderMode(RenderMode.HARDWARE)
        animationView.enableMergePathsForKitKatAndAbove(true)
        animationView.imageAssetsFolder = "images"
        animationView.playAnimation()
        getLoginService()
        // 화면 전환
        jump()
    }

    private fun jump() {
        jwt = getJwt()
        loginId = getLoginId()
        password = getPassword()
        Log.d("Splash-Response", getJwt() + " " + getLoginId() + " " + getPassword())
        if (jwt != null && isAutoLogin() == true) { // 자동 로그인 체크 한 경우
            loginService.autoLogin()
        } else {
            removeAutoLogin()
            changeActivity(LoginActivity::class.java)
            finish()
        }
    }

    private fun getLoginService(){
        loginService = LoginDataService()
        loginService.setAutoLoginView(this)
    }

    private fun changeMainActivity() {
        changeActivity(MainActivity::class.java)
        finish()
    }

    override fun onAutoLoginSuccess(code: Int, result: AutoLoginResult) {
        changeMainActivity()
    }

    override fun onAutoLoginFailure(message: String) {
        removeAutoLogin()
        changeActivity(LoginActivity::class.java)
    }
}