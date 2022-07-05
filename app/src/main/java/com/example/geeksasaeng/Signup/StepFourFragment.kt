package com.example.geeksasaeng.Signup

import android.content.Intent
import android.util.Log
import android.view.View
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.databinding.FragmentStepFourBinding
import java.text.DecimalFormat
import java.util.*
import kotlin.concurrent.timer

class StepFourFragment: BaseFragment<FragmentStepFourBinding>(FragmentStepFourBinding::inflate) {

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var universityName: String? = ""
    var phoneNumber: String? = ""

    private var time = 5000
    private var timerTask : Timer? = null

    override fun initAfterBinding() {
        checkPassword = arguments?.getString("checkPassword")
        loginId = arguments?.getString("loginId")
        nickname = arguments?.getString("nickname")
        password = arguments?.getString("password")
        email = arguments?.getString("email")
        universityName = arguments?.getString("universityName")

        initClickListener()
    }

    private fun initClickListener() {
        binding.stepFourPhoneSendBtn.setOnClickListener {
            binding.stepThreeCheckMsgTv.visibility = View.VISIBLE
            startTimer()
            binding.stepFourPhoneSendBtn.isClickable = false
        }

        binding.stepFourCheckBtn.setOnClickListener {
            resetTimer()
            startTimer()
        }

        binding.stepFourSkipBtn.setOnClickListener {
            val dialog = DialogSignUpPhoneSkip()
            dialog.show(parentFragmentManager, "CustomDialog")
        }

        binding.stepFourNextBtn.setOnClickListener {
            timerTask?.cancel()

            phoneNumber = binding.stepFourPhoneEt.text.toString()

            Log.d("SignupData", "checkPassword = $checkPassword / loginId = $loginId / nickname = $nickname / password = $password / email = $email / universityName = $universityName / phoneNumber = $phoneNumber")

            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("checkPassword", checkPassword)
            intent.putExtra("loginId", loginId)
            intent.putExtra("nickname", nickname)
            intent.putExtra("password", password)
            intent.putExtra("email", email)
            intent.putExtra("universityName", universityName)
            intent.putExtra("phoneNumber", phoneNumber)

            startActivity(intent)
        }
    }

    // 타이머 작동
    private fun startTimer() {
        timerTask = timer(period = 10) {
            val timeForm = DecimalFormat("00")
            val min = timeForm.format(time / 1000)
            val sec = timeForm.format(time / 100)

            activity?.runOnUiThread {
                binding.stepThreeCheckMsgTv.text = "${min}분 ${sec}초 남았어요"

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
        binding.stepThreeCheckMsgTv.text = "05분 00초 남았어요"
    }
}