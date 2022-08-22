package com.example.geeksasaeng.Profile

import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityProfileMyActivityBinding

class ProfileMyActivityActivity: BaseActivity<ActivityProfileMyActivityBinding>(ActivityProfileMyActivityBinding::inflate) {

    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.profileMyActivityBackBtn.setOnClickListener {
            finish()
        }
    }
}