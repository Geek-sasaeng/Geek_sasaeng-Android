package com.example.geeksasaeng.Signup.Naver

import android.content.Intent
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.Signup.Retrofit.SignUpSocialView
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.Retrofit.SocialSignUpRequest
import com.example.geeksasaeng.databinding.FragmentStepNaverThreeBinding

class StepNaverThreeFragment :
    BaseFragment<FragmentStepNaverThreeBinding>(FragmentStepNaverThreeBinding::inflate),
    SignUpSocialView {

    private val progressVM: ProgressNaverViewModel by activityViewModels()
    private val signUpNaverVM: SignUpNaverViewModel by activityViewModels()
    private lateinit var signUpService: SignupDataService

    override fun initAfterBinding() {
        progressVM.increase()
        initClickListener()
    }

    private fun initClickListener() {
        binding.stepNaverThreeStartBtn.setOnClickListener {
            val email = signUpNaverVM.getEmail();
            val nickname = signUpNaverVM.getNickname();
            val universitName = signUpNaverVM.getUniversityName();
            val accessToken = signUpNaverVM.getAccessToken()
            // TODO 이용약관 임시 Y로 해놨는데 나중에 바꾸기
            val socialSignupRequest =
                SocialSignUpRequest(email, "Y", nickname, universitName, accessToken)

            socialRegister(socialSignupRequest)
        }
    }

    private fun socialRegister(socialSignupRequest: SocialSignUpRequest) {
        signUpService = SignupDataService()
        signUpService.setSignUpSocialView(this)
        signUpService.signUpSocialSender(socialSignupRequest)
    }

    override fun onSignUpSocialSuccess() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onSignUpSocialFailure(message: String) {
        TODO("Not yet implemented")
    }
}