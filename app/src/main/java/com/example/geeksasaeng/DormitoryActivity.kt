package com.example.geeksasaeng

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.databinding.ActivityDormitoryBinding

class DormitoryActivity : BaseActivity<ActivityDormitoryBinding>(ActivityDormitoryBinding::inflate) {

    private var name = "이길동" //사용자 이름
    override fun initAfterBinding() {
        val dormitoryArray = arrayOf("제1기숙사","제2기숙사","제3기숙사")
        binding.domitoryPicker.minValue = 0
        binding.domitoryPicker.maxValue = dormitoryArray.size-1
        binding.domitoryPicker.displayedValues = dormitoryArray
        binding.domitoryPicker.value = (dormitoryArray.size-1)/2 //중간값을 초기값으로 세팅

        binding.domitoryHiTv.text = "${name}님,\n환영합니다"
        initDormPicker()
    }

    private fun initDormPicker(){
        binding.domitoryPicker.setOnValueChangedListener { picker, oldVal, newVal ->

        }
    }
    
}