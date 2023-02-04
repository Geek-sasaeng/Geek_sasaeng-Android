package com.example.geeksasaeng.Profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.geeksasaeng.Profile.Retrofit.ProfileDataService
import com.example.geeksasaeng.Profile.Retrofit.ProfilePasswordChangeRequest
import com.example.geeksasaeng.Profile.Retrofit.ProfilePasswordChangeView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.ActivityProfileMyInfoUpdatePwdBinding
import java.util.regex.Pattern

class ProfileMyInfoUpdatePwdActivity: BaseActivity<ActivityProfileMyInfoUpdatePwdBinding>(ActivityProfileMyInfoUpdatePwdBinding::inflate), ProfilePasswordChangeView {

    lateinit var profiledataService: ProfileDataService

    override fun initAfterBinding() {
        profiledataService = ProfileDataService()
        profiledataService.setProfilePasswordChangeView(this)
        initClickListener()
        initTextWatcher()
    }

    private fun initClickListener() {
        binding.profileMyInfoUpdatePwdBackBtn.setOnClickListener { //뒤로가기
            finish()
        }

        binding.profileMyInfoUpdatePwdCompleteTv.setOnClickListener { //완료
            val profilePasswordChangeRequest = ProfilePasswordChangeRequest(binding.profileMyInfoUpdatePwdCheckEt.text.toString(), binding.profileMyInfoUpdatePwdEt.text.toString())
            profiledataService.profilePasswordChangeSender(profilePasswordChangeRequest)
        }
    }

    private fun initTextWatcher() {
        binding.profileMyInfoUpdatePwdEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {

                val pwPattern = Pattern.compile("""^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^+\-=])(?=\S+$)[A-Za-z\d!@#$%^+\-=]{8,}$""") //패턴 생성 (문자,숫자,특수문자 포함 8자 이상)
                val macher = pwPattern.matcher(binding.profileMyInfoUpdatePwdEt.text.toString())

                if (macher.matches()) { //조건식에 맞는 비밀번호가 들어오면
                    binding.profileMyInfoUpdatePwdInfoTv.visibility = View.INVISIBLE
                } else{
                    binding.profileMyInfoUpdatePwdEt.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.error) // 밑줄 색 빨간 색으로
                    if(binding.profileMyInfoUpdatePwdInfoTv.visibility == View.INVISIBLE){
                        binding.profileMyInfoUpdatePwdInfoTv.visibility = View.VISIBLE // 보이게 만들기
                    }
                }
            }
        })
    }

    override fun onProfilePasswordChangeSuccess() {
        CustomToastMsg.createToast(this, "비밀번호를 변경하였습니다", "#8029ABE2", 53)?.show()
        //TODO: 나의 정보 화면 = ProfileFragment로 이동하기
    }

    override fun onProfilePasswordChangeFailure(message: String) {
        Log.d("passwordChange", message)
    }
}