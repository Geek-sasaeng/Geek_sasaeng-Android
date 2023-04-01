package com.example.geeksasaeng.Profile.ProfileCard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityProfileLevelIntroductionBinding
import com.example.geeksasaeng.databinding.ActivityProfileMyActivityBinding

class ProfileLevelIntroductionActivity : BaseActivity<ActivityProfileLevelIntroductionBinding>(ActivityProfileLevelIntroductionBinding::inflate){

    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener() {

        binding.profileLevelIntroductionGoMainBtn.setOnClickListener {
            //홈 화면으로 이동하기
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.profileLevelIntroductionBackBtn.setOnClickListener {
            finish()
        }
    }
}