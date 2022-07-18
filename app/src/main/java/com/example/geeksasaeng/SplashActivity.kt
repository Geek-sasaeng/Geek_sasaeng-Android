package com.example.geeksasaeng

import android.os.Handler
import android.os.Looper
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.databinding.ActivitySplashBinding

class SplashActivity: BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun initAfterBinding() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            // 테스트
            /*changeActivity(MainActivity::class.java)*/
            changeActivity(LoginActivity::class.java)
            finish()
        }, 1500)
    }
}