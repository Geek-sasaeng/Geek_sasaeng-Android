package com.example.geeksasaeng

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.databinding.ActivityDormitoryBinding

class DormitoryActivity : BaseActivity<ActivityDormitoryBinding>(ActivityDormitoryBinding::inflate) {

    private var name = "" //사용자 이름
    override fun initAfterBinding() {
        binding.domitoryHiTv.text = "{name}님,\n환영합니다"
    }



}