package com.example.geeksasaeng.Signup.Basic

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.Home.Delivery.Adapter.PeopleSpinnerAdapter
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.DialogSignUpEmailCheck
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.databinding.FragmentStepTwoBinding
import com.example.geeksasaeng.util.getUuid

class StepTwoFragment : BaseFragment<FragmentStepTwoBinding>(FragmentStepTwoBinding::inflate), SignUpEmailView {

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var university: String? = ""

    var universityList: Array<String> = arrayOf("자신의 학교를 선택해주세요", "ㄱ", "가천대학교", "ㄴ", "나천대학교", "ㄷ", "다천대학교", "ㄹ", "라천대학교", "ㅁ", "마천대학교")

    var uuid: String? = null

    private lateinit var signUpService : SignupDataService

    private val progressVM: ProgressViewModel by activityViewModels()

    override fun initAfterBinding() {
        Log.d("EMAIL-RESPONSE", "StepTwoFragment : InitAfterBinding")

        progressVM.increase()

        checkPassword = arguments?.getString("checkPassword")
        loginId = arguments?.getString("loginId")
        nickname = arguments?.getString("nickname")
        password = arguments?.getString("password")

        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setSignUpEmailView(this@StepTwoFragment)

        showToast(binding.stepTwoSchoolSp.selectedItem.toString())

        initSpinner()
        initClickListener()
    }

    //스피너 관련 작업
    private fun initSpinner(){
        val spinnerAdapter = UniversitySpinnerAdapter(requireContext(), universityList)
        binding.stepTwoSchoolSp.adapter = spinnerAdapter
        binding.stepTwoSchoolSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 축소된 스피너화면에 맞게 아이템 색상, 화살표 변경
                val image: ImageView = view!!.findViewById(R.id.university_arrow_iv)
                image.setImageResource(R.drawable.ic_spinner_up)
                image.visibility = View.VISIBLE

                universityList[0] = universityList[position] // items[0]은 현재 선택된 아이템 저장용
                val textName: TextView = view!!.findViewById(R.id.spinner_university_text)
                textName.text = universityList[position]
                university = textName.text.toString()
                Log.d("UNIVERSITY-RESPONSE", university!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun initClickListener() {
        // 이메일 인증 전송 버튼
        binding.stepTwoEmailCheckBtn.setOnClickListener {
            sendEmail()
        }

        binding.stepTwoNextBtn.setOnClickListener {
            val transaction: FragmentTransaction =
                (context as SignUpActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("checkPassword", checkPassword)
            bundle.putString("loginId", loginId)
            bundle.putString("nickname", nickname)
            bundle.putString("password", password)

            email = binding.stepTwoEmailEt.text.toString() + "@" + binding.stepTwoEmail2Et.text.toString()
            // university = binding.stepTwoSchoolEt.text.toString()

            bundle.putString("email", email)
            // bundle.putString("universityName", university)

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

    private fun sendEmail() {
        email = binding.stepTwoEmailEt.text.toString() + "@" + binding.stepTwoEmail2Et.text.toString()

        val uuid = getUuid().toString()
        val signUpEmailRequest = SignUpEmailRequest(email, university, uuid)
        signUpService.signUpEmailSender(signUpEmailRequest)
    }

    override fun onSignUpEmailSuccess(message: String) {
        Log.d("EMAIL-RESPONSE", message)
//        val dialog = DialogSignUpEmailCheck()
//        dialog.show(parentFragmentManager, "CustomDialog")
//
//        // 1.5초 뒤에 Dialog 자동 종료
//        Handler().postDelayed({
//            dialog.dismiss()
//        }, 1500)
    }

    override fun onSignUpEmailFailure(code: Int, message: String) {
        Log.d("EMAIL-RESPONSE", "StepTwoFragment : onSignUpEmailFailure : Fail Code = $code")

        when(code){
            2803 -> showToast(message)
            2804 -> showToast(message)
            2400 -> showToast(message)
        }
    }
}