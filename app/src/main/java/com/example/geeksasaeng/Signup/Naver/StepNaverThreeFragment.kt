package com.example.geeksasaeng.Signup.Naver

import android.content.Intent
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment.Companion.privacyTemrsAgree
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment.Companion.serviceTemrsAgree
import com.example.geeksasaeng.Signup.Retrofit.SignUpSocialView
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.Retrofit.SocialSignUpRequest
import com.example.geeksasaeng.Signup.Tos1Activity
import com.example.geeksasaeng.Signup.Tos2Activity
import com.example.geeksasaeng.databinding.FragmentStepNaverThreeBinding

class StepNaverThreeFragment :
    BaseFragment<FragmentStepNaverThreeBinding>(FragmentStepNaverThreeBinding::inflate),
    SignUpSocialView {

    companion object{
        var socialServiceTemrsAgree: Boolean = false
        var socialPrivacyTemrsAgree: Boolean = false
    }

    private val progressVM: ProgressNaverViewModel by activityViewModels()
    private val signUpNaverVM: SignUpNaverViewModel by activityViewModels()
    private lateinit var signUpService: SignupDataService

    override fun onStart() {
        super.onStart()
        checkTermsAgree()
    }

    override fun initAfterBinding() {
        progressVM.setValue(3)
        initClickListener()
    }

    private fun initClickListener() {

        binding.stepNaverThreeAgree1MoreIv.setOnClickListener {
            val intent = Intent(activity, Tos1Activity::class.java)
            intent.putExtra("isSocial", true)
            startActivity(intent)
        }

        binding.stepNaverThreeAgree2MoreIv.setOnClickListener {
            val intent = Intent(activity, Tos2Activity::class.java)
            intent.putExtra("isSocial", true)
            startActivity(intent)
        }

        binding.stepNaverThreeAgree1Cb.setOnCheckedChangeListener { buttonView, isChecked ->
            socialServiceTemrsAgree = isChecked
            if(socialPrivacyTemrsAgree){
                binding.stepNaverThreeAgreeAllCb.isChecked = socialServiceTemrsAgree&& socialPrivacyTemrsAgree
                binding.stepNaverThreeAgree2Cb.isChecked = true
            }

        }

        binding.stepNaverThreeAgree2Cb.setOnCheckedChangeListener { buttonView, isChecked ->
            socialPrivacyTemrsAgree = isChecked
            if(socialServiceTemrsAgree){
                binding.stepNaverThreeAgreeAllCb.isChecked = socialServiceTemrsAgree&& socialPrivacyTemrsAgree
                binding.stepNaverThreeAgree1Cb.isChecked = true
            }
        }

        binding.stepNaverThreeAgreeAllCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.stepNaverThreeAgree1Cb.isChecked = true
                binding.stepNaverThreeAgree2Cb.isChecked = true
                socialServiceTemrsAgree = true
                socialPrivacyTemrsAgree = true
            }else{
                binding.stepNaverThreeAgree1Cb.isChecked = false
                binding.stepNaverThreeAgree2Cb.isChecked = false
                socialServiceTemrsAgree = false
                socialPrivacyTemrsAgree = false
            }
        }

        binding.stepNaverThreeStartBtn.setOnClickListener {
            if(socialServiceTemrsAgree&& socialPrivacyTemrsAgree){
                signUpNaverVM.setInformationAgreeStatus("Y")
                val email = signUpNaverVM.getEmail()
                val nickname = signUpNaverVM.getNickname()
                val universitName = signUpNaverVM.getUniversityName()
                val accessToken = signUpNaverVM.getAccessToken()
                val infomationAgreeStatus = signUpNaverVM.getInformationAgreeStatus()

                val socialSignupRequest =
                    SocialSignUpRequest(email, infomationAgreeStatus, nickname, universitName, accessToken)

                socialRegister(socialSignupRequest)
            }else{
                showToast("이용 약관을 모두 체크하셔야 회원가입 진행을 하실 수 있습니다.")
            }
        }
    }

    fun checkTermsAgree(){
        if(socialServiceTemrsAgree){
            binding.stepNaverThreeAgree1Cb.isChecked = true
        }
        if(socialPrivacyTemrsAgree){
            binding.stepNaverThreeAgree2Cb.isChecked = true
        }
    }

    private fun socialRegister(socialSignupRequest: SocialSignUpRequest) {
        signUpService = SignupDataService() // 서비스 객체 생성
        signUpService.setSignUpSocialView(this)
        signUpService.signUpSocialSender(socialSignupRequest)
    }

    override fun onSignUpSocialSuccess() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onSignUpSocialFailure(message: String) {

    }
}