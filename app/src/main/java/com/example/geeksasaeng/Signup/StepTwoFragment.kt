package com.example.geeksasaeng.Signup

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.Home.CreateParty.DialogDt
import com.example.geeksasaeng.R
import com.example.geeksasaeng.SignUpActivity
import com.example.geeksasaeng.databinding.FragmentStepTwoBinding

class StepTwoFragment : BaseFragment<FragmentStepTwoBinding>(FragmentStepTwoBinding::inflate) {

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""

    private lateinit var progressVM: ProgressViewModel

    override fun initAfterBinding() {
        progressVM = ViewModelProvider(this).get(ProgressViewModel::class.java)

        progressVM.increase()
        Log.d("PROGRESS-STATUS", "TWO = " + progressVM.currentPro.value.toString())

        checkPassword = arguments?.getString("checkPassword")
        loginId = arguments?.getString("loginId")
        nickname = arguments?.getString("nickname")
        password = arguments?.getString("password")

        initClickListener()
    }

    private fun initClickListener() {

        // 이메일 인증 전송 버튼
        binding.stepTwoEmailCheckBtn.setOnClickListener {
            val dialog = DialogSignUpEmailCheck()
            dialog.show(parentFragmentManager, "CustomDialog")

            // 1.5초 뒤에 Dialog 자동 종료
            Handler().postDelayed({
                dialog.dismiss()
            }, 1500)
        }

        binding.stepTwoNextBtn.setOnClickListener {
            val transaction: FragmentTransaction =
                (context as SignUpActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("checkPassword", checkPassword)
            bundle.putString("loginId", loginId)
            bundle.putString("nickname", nickname)
            bundle.putString("password", password)
            bundle.putString("email", binding.stepTwoEmailEt.text.toString() + "@" + binding.stepTwoEmail2Et.text.toString())
            bundle.putString("universityName", binding.stepTwoSchoolEt.text.toString())

            val stepThreeFragment = StepThreeFragment()
            stepThreeFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_vp, stepThreeFragment).commit()

            stepThreeFragment.arguments = bundle

            transaction.replace(R.id.sign_up_vp, stepThreeFragment)
            transaction.commit()
        }
    }
}
