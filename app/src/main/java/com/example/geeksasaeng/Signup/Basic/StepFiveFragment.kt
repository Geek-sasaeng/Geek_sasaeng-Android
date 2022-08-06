package com.example.geeksasaeng.Signup.Basic

import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Retrofit.SignUpView
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.Signup.Tos1Activity
import com.example.geeksasaeng.Signup.Tos2Activity
import com.example.geeksasaeng.databinding.FragmentStepFiveBinding
import okhttp3.internal.notifyAll

class StepFiveFragment : BaseFragment<FragmentStepFiveBinding>(FragmentStepFiveBinding::inflate), SignUpView, Tos1Activity.AgreeClickListener {

    private val progressVM: ProgressViewModel by activityViewModels()
    private val signUpVM: SignUpViewModel by activityViewModels()

    private lateinit var signUpService : SignupDataService
    private var firstRead1 : Boolean = true
    private var firstRead2 : Boolean = true
    private var isAgree:  Boolean = false

    override fun onStart() {
        super.onStart()
        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setSignUpView(this)
    }

    override fun initAfterBinding() {
        progressVM.increase()
        initClickListener()
    }

    private fun initClickListener() {

        binding.stepFiveAgree1MoreIv.setOnClickListener {
            val intent = Intent(activity, Tos1Activity::class.java)
            intent.getBooleanExtra("isSocial", false)
            startActivity(intent)
            firstRead1 = false
        }

        binding.stepFiveAgree2MoreIv.setOnClickListener {
            val intent = Intent(activity, Tos2Activity::class.java)
            intent.getBooleanExtra("isSocial", false)
            startActivity(intent)
            firstRead2 = false
        }

        binding.stepFiveAgree1Cb.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked&&firstRead1){
                val intent = Intent(activity, Tos1Activity::class.java)
                intent.getBooleanExtra("isSocial", false)
                startActivity(intent)
                firstRead1 = false
            }
        }

        binding.stepFiveAgree2Cb.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked&&firstRead2){
                val intent = Intent(activity, Tos2Activity::class.java)
                intent.getBooleanExtra("isSocial", false)
                startActivity(intent)
                firstRead2 = false
            }
        }

        binding.stepFiveAgreeAllCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.stepFiveAgree1Cb.isChecked = true
                binding.stepFiveAgree2Cb.isChecked = true
            } else {
                binding.stepFiveAgree1Cb.isChecked = false
                binding.stepFiveAgree2Cb.isChecked = false
            }
        }

        binding.stepFiveStartBtn.setOnClickListener {
            signUpVM.setInformationAgreeStatus("Y")

            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("checkPassword", signUpVM.getCheckPassword())
            intent.putExtra("emailId", signUpVM.getEmailId())
            // TODO: 약관페이지는 디자인이 안나와서 구현X 일단 DEFAULT값으로 Y줌
            intent.putExtra("informationAgreeStatus", signUpVM.getInformationAgreeStatus())
            intent.putExtra("loginId", signUpVM.getLoginId())
            intent.putExtra("nickname", signUpVM.getNickname())
            intent.putExtra("password", signUpVM.getPassword())
            intent.putExtra("phoneNumberId", signUpVM.getPhoneNumberId())
            intent.putExtra("universityName", signUpVM.getUniversityName())
            startActivity(intent)
        }
    }

    //회원가입 성공/실패
    override fun onSignUpSuccess() {
        Log.d("signup", "회원가입에 성공하였습니다.")
    }

    override fun onSignUpFailure(message:String) {
        showToast(message)
    }

    override fun onAgreeClicked(agree: Boolean) {
        Log.d("cherry",agree.toString()+"받아옴")
        isAgree = agree
        binding.stepFiveAgree1Cb.isChecked = isAgree
    }

}