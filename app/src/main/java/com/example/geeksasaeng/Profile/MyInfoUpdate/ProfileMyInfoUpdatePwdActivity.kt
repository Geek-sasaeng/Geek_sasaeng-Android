package com.example.geeksasaeng.Profile.MyInfoUpdate

import android.content.Intent
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.geeksasaeng.MainActivity
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
            Log.d("passwordChange", "완료 눌러짐")
            val profilePasswordChangeRequest = ProfilePasswordChangeRequest(binding.profileMyInfoUpdatePwdCheckEt.text.toString(), binding.profileMyInfoUpdatePwdEt.text.toString())
            profiledataService.profilePasswordChangeSender(profilePasswordChangeRequest)
        }

        binding.profileMyInfoPwdEyeBtn.setOnClickListener { //비밀번호 눈 아이콘 토글버튼
            if ((binding.profileMyInfoUpdatePwdEt.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) or (binding.profileMyInfoUpdatePwdEt.inputType == 129)) { //왜 129로 뜰까?? 이상하네 (InputType.TYPE_TEXT_VARIATION_PASSWORD이 128인데)
                binding.profileMyInfoUpdatePwdEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.profileMyInfoPwdEyeBtn.setImageResource(R.drawable.ic_pwd_on)
            } else {
                binding.profileMyInfoUpdatePwdEt.inputType =  InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.profileMyInfoPwdEyeBtn.setImageResource(R.drawable.ic_pwd_off  )
            }
        }

        binding.profileMyInfoPwdCheckEyeBtn.setOnClickListener { //비밀번호 눈 아이콘 토글버튼
            if ((binding.profileMyInfoUpdatePwdCheckEt.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) or (binding.profileMyInfoUpdatePwdCheckEt.inputType == 129)) { //왜 129로 뜰까?? 이상하네 (InputType.TYPE_TEXT_VARIATION_PASSWORD이 128인데)
                binding.profileMyInfoUpdatePwdCheckEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.profileMyInfoPwdCheckEyeBtn.setImageResource(R.drawable.ic_pwd_on)
            } else {
                binding.profileMyInfoUpdatePwdCheckEt.inputType =  InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.profileMyInfoPwdCheckEyeBtn.setImageResource(R.drawable.ic_pwd_off  )
            }
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
                    binding.profileMyInfoUpdatePwdInfoTv.visibility = View.GONE
                    binding.profileMyInfoUpdatePwdLine.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.gray_0)
                } else{
                    binding.profileMyInfoUpdatePwdLine.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.error) // 밑줄 색 빨간 색으로
                    if(binding.profileMyInfoUpdatePwdInfoTv.visibility == View.GONE){
                        binding.profileMyInfoUpdatePwdInfoTv.visibility = View.VISIBLE // 보이게 만들기
                    }
                }
                checkingNext()
            }
        })

        binding.profileMyInfoUpdatePwdCheckEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {

                if (binding.profileMyInfoUpdatePwdCheckEt.text.toString() == binding.profileMyInfoUpdatePwdEt.text.toString()) {
                    binding.profileMyInfoUpdatePwdCheckInfoTv.visibility = View.GONE
                    binding.profileMyInfoUpdatePwdCheckLine.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.gray_0)
                } else{
                    binding.profileMyInfoUpdatePwdCheckLine.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.error) // 밑줄 색 빨간 색으로
                    if(binding.profileMyInfoUpdatePwdCheckInfoTv.visibility == View.GONE){
                        binding.profileMyInfoUpdatePwdCheckInfoTv.visibility = View.VISIBLE // 보이게 만들기
                    }
                }
                checkingNext()
            }
        })
    }

    private fun checkingNext(){
        if (binding.profileMyInfoUpdatePwdInfoTv.visibility == View.GONE && binding.profileMyInfoUpdatePwdCheckInfoTv.visibility ==View.GONE){
            binding.profileMyInfoUpdatePwdCompleteTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.main))
            binding.profileMyInfoUpdatePwdCompleteTv.isEnabled = true
        }else{
            binding.profileMyInfoUpdatePwdCompleteTv.setTextColor(ContextCompat.getColor(applicationContext, R.color.bababa_color))
            binding.profileMyInfoUpdatePwdCompleteTv.isEnabled = false
        }
    }

    override fun onProfilePasswordChangeSuccess() {
        Log.d("passwordChange", "비밀번호 변경 성공")
        CustomToastMsg.createToast(this, "비밀번호를 변경하였습니다", "#8029ABE2", 53)?.show()
        //나의 정보 화면 = ProfileFragment로 이동하기
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("status", "ProfileFragment")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onProfilePasswordChangeFailure(message: String) {
        Log.d("passwordChange", message)
    }
}