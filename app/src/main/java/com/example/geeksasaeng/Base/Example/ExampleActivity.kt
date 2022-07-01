package com.example.geeksasaeng.Base.Example

import android.os.Bundle
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ActivityExampleBinding

class ExampleActivity : BaseActivity<ActivityExampleBinding>(ActivityExampleBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Example
        binding.btn1.setOnClickListener {
            changeActivity(MainActivity::class.java)
        }

        binding.btn2.setOnClickListener {
            setFragment(R.id.main_fl, ExampleFragment())
        }
    }
}