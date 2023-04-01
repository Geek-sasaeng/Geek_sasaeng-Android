package com.example.geeksasaeng.Profile.CustomerService

import android.content.Intent
import android.net.Uri
import com.example.geeksasaeng.Signup.Tos1Activity
import com.example.geeksasaeng.Signup.Tos2Activity
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityProfileCustomerServiceBinding

class ProfileCustomerServiceActivity: BaseActivity<ActivityProfileCustomerServiceBinding>(ActivityProfileCustomerServiceBinding::inflate) {
    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener() {

        binding.profileCustomerServiceBackBtn.setOnClickListener {
            finish()
        }

        binding.profileCustomerServiceTos1.setOnClickListener {
            val intent = Intent(this, Tos2Activity::class.java)
            intent.putExtra("status","profile")
            startActivity(intent)
        }

        binding.profileCustomerServiceTos2.setOnClickListener {
            val intent = Intent(this, Tos1Activity::class.java)
            intent.putExtra("status","profile")
            startActivity(intent)
        }

        binding.profileCustomerServiceKakaoTalkCv.setOnClickListener {
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_Sxolhxj")) //긱사생 카카오톡 플러스 친구 링크
            startActivity(urlIntent)
        }
    }
}