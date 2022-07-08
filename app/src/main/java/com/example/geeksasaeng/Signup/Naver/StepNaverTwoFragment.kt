package com.example.geeksasaeng.Signup.Naver

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.FragmentStepNaverTwoBinding
import java.text.DecimalFormat
import java.util.*
import kotlin.concurrent.timer

class StepNaverTwoFragment : BaseFragment<FragmentStepNaverTwoBinding>(FragmentStepNaverTwoBinding::inflate) {

    var nickname: String? = ""
    var email: String? = ""
    var universityName: String? = ""

    private val progressVM: ProgressNaverViewModel by activityViewModels()

    private var time = 5000
    private var timerTask : Timer? = null

    override fun initAfterBinding() {
        progressVM.increase()
        
        startTimer()

        nickname = arguments?.getString("nickname")
        universityName = arguments?.getString("universityName")
        email = arguments?.getString("email")

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepNaverThreeSendBtn.setOnClickListener {
            resetTimer()
            startTimer()
        }

        binding.stepNaverThreeNextBtn.setOnClickListener {
            timerTask?.cancel()

            val transaction: FragmentTransaction =
                (context as SignUpNaverActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("nickname", nickname)
            bundle.putString("email", email)
            bundle.putString("universityName", universityName)

            val stepNaverThreeFragment = StepNaverThreeFragment()
            stepNaverThreeFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpNaverActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_naver_vp, stepNaverThreeFragment).commit()

            stepNaverThreeFragment.arguments = bundle

            transaction.replace(R.id.sign_up_naver_vp, stepNaverThreeFragment)
            transaction.commit()
        }
    }

    // 타이머 작동
    private fun startTimer() {
        timerTask = timer(period = 10) {
            val timeForm = DecimalFormat("00")
            val min = timeForm.format(time / 1000)
            val sec = timeForm.format(time / 100)

            activity?.runOnUiThread {
                binding.stepNaverThreeCheckMsgTv.text = "${min}분 ${sec}초 남았어요"

                if (min == "00" && sec == "00")
                    timerTask?.cancel()
            }

            time--
        }
    }

    // 타이머 초기화
    private fun resetTimer() {
        timerTask?.cancel()

        time = 5000
        binding.stepNaverThreeCheckMsgTv.text = "05분 00초 남았어요"
    }
}