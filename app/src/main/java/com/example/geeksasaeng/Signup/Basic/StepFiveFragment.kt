package com.example.geeksasaeng.Signup.Basic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Retrofit.SignUpRequest
import com.example.geeksasaeng.Signup.Retrofit.SignUpView
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.Tos1Activity
import com.example.geeksasaeng.Signup.Tos2Activity
import com.example.geeksasaeng.databinding.FragmentStepFiveBinding
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import okhttp3.internal.notifyAll

class StepFiveFragment : BaseFragment<FragmentStepFiveBinding>(FragmentStepFiveBinding::inflate), SignUpView{
    companion object{
        var serviceTemrsAgree: Boolean = false
        var privacyTemrsAgree: Boolean = false
    }
    private val progressVM: ProgressViewModel by activityViewModels()
    private val signUpVM: SignUpViewModel by activityViewModels()
    private lateinit var signUpService : SignupDataService

    override fun onStart() {
        super.onStart()
        progressVM.setValue(5)
        checkTermsAgree()
        signUpSetting()
    }

    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.stepFiveAgree1MoreIv.setOnClickListener {
            val intent = Intent(activity, Tos1Activity::class.java)
            intent.getBooleanExtra("isSocial", false)
            startActivity(intent)
        }

        binding.stepFiveAgree2MoreIv.setOnClickListener {
            val intent = Intent(activity, Tos2Activity::class.java)
            intent.getBooleanExtra("isSocial", false)
            startActivity(intent)
        }

        binding.stepFiveAgree1Cb.setOnCheckedChangeListener { buttonView, isChecked ->
            serviceTemrsAgree = isChecked
            if(privacyTemrsAgree){
                binding.stepFiveAgreeAllCb.isChecked = privacyTemrsAgree&& serviceTemrsAgree
                binding.stepFiveAgree2Cb.isChecked = true
            }
        }

        binding.stepFiveAgree2Cb.setOnCheckedChangeListener { buttonView, isChecked ->
            privacyTemrsAgree = isChecked
            if(serviceTemrsAgree){
                binding.stepFiveAgreeAllCb.isChecked = privacyTemrsAgree&& serviceTemrsAgree
                binding.stepFiveAgree1Cb.isChecked=true
            }

        }

        binding.stepFiveAgreeAllCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.stepFiveAgree1Cb.isChecked = true
                binding.stepFiveAgree2Cb.isChecked = true
                serviceTemrsAgree = true
                privacyTemrsAgree = true
            } else {
                binding.stepFiveAgree1Cb.isChecked = false
                binding.stepFiveAgree2Cb.isChecked = false
                serviceTemrsAgree = false
                privacyTemrsAgree = false
            }
        }

        binding.stepFiveStartBtn.setOnClickListener {
            if(serviceTemrsAgree && privacyTemrsAgree){
                signUp()
            }else{
                showToast("이용 약관을 모두 체크하셔야 회원가입 진행을 하실 수 있습니다.")
            }
        }
    }

    fun checkTermsAgree(){
        if(serviceTemrsAgree){
            binding.stepFiveAgree1Cb.isChecked = true
        }
        if(privacyTemrsAgree){
            binding.stepFiveAgree2Cb.isChecked = true
        }
    }
    fun signUpSetting(){
        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setSignUpView(this)
    }

    fun signUp(){
        signUpVM.setInformationAgreeStatus("Y")
        val checkPassword = signUpVM.getCheckPassword()!!
        val emailId = signUpVM.getEmailId()
        val informationAgreeStatus = signUpVM.getInformationAgreeStatus()
        val loginId = signUpVM.getLoginId()
        val nickName = signUpVM.getNickname()
        val password = signUpVM.getPassword()!!
        val phoneNumberId = signUpVM.getPhoneNumberId()
        val universityName = signUpVM.getUniversityName()
        val signUpRequest = SignUpRequest(checkPassword, emailId,informationAgreeStatus, loginId,
            nickName, password, phoneNumberId, universityName)
        signUpService.signUpSender(signUpRequest)
    }
    //회원가입 성공/실패
    override fun onSignUpSuccess() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        Log.d("signup", "회원가입에 성공하셨습니다.")
        showToast("회원가입에 성공하셨습니다.")
        activity?.finish()
    }

    override fun onSignUpFailure(message:String) {
        // 2006 : 중복되는 유저 아이디
        // 2007 : 중복되는 유저 이메일
        // 2008 : 존재하지 않는 학교 이름
        // 2201 : 회원 정보 동의 Status가 Y가 아닌 경우
        // 2205 : 존재하지 않는 회원 ID
        // 4000 : 서버 오류
        showToast("서버 오류")
        showToast(message)
    }
}