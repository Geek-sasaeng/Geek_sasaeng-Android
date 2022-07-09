package com.example.geeksasaeng

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.databinding.ActivitySplashBinding

class SplashActivity: BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun initAfterBinding() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            changeActivity(LoginActivity::class.java)
            finish()
        }, 1500)
    }
}