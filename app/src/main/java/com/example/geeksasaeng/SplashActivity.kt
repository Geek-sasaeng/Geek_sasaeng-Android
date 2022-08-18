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
        Log.d("jwt", "Bearer " + getJwt())
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
        Log.d("splash", "jump함수 작동")
        jwt = getJwt()
        loginId = getLoginId() // TODO: 이거 필요해?
        password = getPassword() // TODO: 이거 필요해?
        //Splash-Response
        Log.d("splash-response", getJwt() + " " + getLoginId() + " " + getPassword()+ "/"+ getDormitoryId().toString())
        if (jwt != null && isAutoLogin() == true && getDormitoryId()!=-1) { // 자동 로그인 체크 한 경우 + 기숙사 설정을 완료한 경우(dormitoryId가 -1이 불러와지면 기숙사 설정을 안한 상태이므로 자동로그인이 체크되어있어도, 로그인부터 다시하게 함)
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