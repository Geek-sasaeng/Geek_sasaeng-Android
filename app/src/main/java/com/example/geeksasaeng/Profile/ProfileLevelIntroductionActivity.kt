package com.example.geeksasaeng.Profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityProfileLevelIntroductionBinding
import com.example.geeksasaeng.databinding.ActivityProfileMyActivityBinding

class ProfileLevelIntroductionActivity : BaseActivity<ActivityProfileLevelIntroductionBinding>(ActivityProfileLevelIntroductionBinding::inflate){

    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.profileLevelIntroductionBackBtn.setOnClickListener {
            finish()
        }
    }
}