package com.example.geeksasaeng.Signup

import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityTos2Binding

class Tos2Activity : BaseActivity<ActivityTos2Binding>(ActivityTos2Binding::inflate) {

    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener(){
        binding.tos2BackBtn.setOnClickListener { // 뒤로가기버튼
            onBackPressed()
            //finish()
        }
    }

}