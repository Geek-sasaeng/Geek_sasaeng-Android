package com.example.geeksasaeng

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Base.Example.ExampleFragment
import com.example.geeksasaeng.databinding.ActivitySplashBinding

class SplashActivity: BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            // Test
            // changeActivity(MainActivity::class.java)

            // Original
            changeActivity(LoginActivity::class.java)
            finish()
        }, 1500)
    }
}