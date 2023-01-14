package com.example.geeksasaeng.Profile

import android.os.Bundle
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityProfileMyInfoUpdateBinding

class ProfileMyInfoUpdateActivity: BaseActivity<ActivityProfileMyInfoUpdateBinding>(ActivityProfileMyInfoUpdateBinding::inflate) {

    private var dormitoryId = 1 //default 기숙사 아이디
    override fun initAfterBinding() {
        initClickListener()
        initRadioButton()
    }

    private fun initRadioButton() { // 기숙사 선택 라디오 버튼

        binding.profileMyInfoUpdateDormitoryRg1.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) { // checkId != -1이 무슨 뜻이더랑..!
                binding.profileMyInfoUpdateDormitoryRg2.clearCheck()
                binding.profileMyInfoUpdateDormitoryRg1.check(checkedId)
            }
            when(checkedId){
                R.id.profile_my_info_update_dormitory_rb1 -> dormitoryId = 1
                R.id.profile_my_info_update_dormitory_rb2 -> dormitoryId = 2
                R.id.profile_my_info_update_dormitory_rb3 -> dormitoryId = 3
                else-> {}
            }
        }

        binding.profileMyInfoUpdateDormitoryRg2.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                binding.profileMyInfoUpdateDormitoryRg1.clearCheck() // 1번째열의 check는 지워주기
                binding.profileMyInfoUpdateDormitoryRg2.check(checkedId)
            }
            when(checkedId){
                R.id.profile_my_info_update_dormitory_rb4 -> dormitoryId = 4
                R.id.profile_my_info_update_dormitory_rb5 -> dormitoryId = 5
                R.id.profile_my_info_update_dormitory_rb6 -> dormitoryId = 6
                else-> {}
            }
        }
    }

    private fun initClickListener() {
        binding.profileMyInfoUpdateBackBtn.setOnClickListener { //뒤로
            finish()
        }

        binding.profileMyInfoUpdateCompleteTv.setOnClickListener {
            val dialogProfileUpdate = DialogProfileUpdate()
            val bundle = Bundle()

            dialogProfileUpdate.arguments= bundle
            dialogProfileUpdate.show(supportFragmentManager, "DialogProfileUpdate")
        }
    }
}