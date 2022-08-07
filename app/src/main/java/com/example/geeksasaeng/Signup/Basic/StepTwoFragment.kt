package com.example.geeksasaeng.Signup.Basic

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Login.LoginActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Retrofit.*
import com.example.geeksasaeng.Signup.ToastMsgSignup
import com.example.geeksasaeng.Signup.UniversitySpinnerAdapter
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.FragmentStepTwoBinding
import com.example.geeksasaeng.Utils.getUuid
import com.navercorp.nid.oauth.NidOAuthPreferencesManager.code
import java.text.DecimalFormat
import java.util.*
import kotlin.concurrent.timer

class StepTwoFragment : BaseFragment<FragmentStepTwoBinding>(FragmentStepTwoBinding::inflate), SignUpEmailView {

    var email: String? = ""
    var university: String? = ""

    var universityList: Array<String> = arrayOf("자신의 학교를 선택해주세요", "ㄱ", "가천대학교","자신의 학교를 선택해주세요")
    //var universityList: Array<String> = arrayOf("자신의 학교를 선택해주세요", "ㄱ", "가천대학교", "ㄴ", "나천대학교", "ㄷ", "다천대학교", "ㄹ", "라천대학교", "ㅁ", "마천대학교")

    private lateinit var signUpService : SignupDataService

    private val progressVM: ProgressViewModel by activityViewModels()
    private val signUpVM: SignUpViewModel by activityViewModels()

    private var time = 300000 //5분은 300초 = 300*1000
    private var timerTask : Timer? = null

    private var isSendEmail: Boolean = false

    override fun onStart() {
        super.onStart()
        progressVM.setValue(2)
        if (signUpVM.getEmail().toString()!="null"){
            var results = signUpVM.getEmail().toString().split("@")
            binding.stepTwoEmailEt.setText(results[0])
        }
    }

    override fun initAfterBinding() {

        signUpService = SignupDataService() //서비스 객체 생성
        signUpService.setSignUpEmailView(this@StepTwoFragment)

        initSpinner()
        initTextChangedListener()
        initClickListener()
    }

    // 타이머 작동
    private fun startTimer() {
        timerTask = timer(period = 1000) { //1초가 주기
            if (time != 0) // time이 0이 아니라면
                time -= 1000 //1초씩 줄이기
        }
    }

    override fun onStop() {
        super.onStop()
        timerTask?.cancel() //화면 꺼질때
    }

    //스피너 관련 작업
    private fun initSpinner() {
        val spinnerAdapter = UniversitySpinnerAdapter(requireContext(), universityList)
        binding.stepTwoSchoolSp.adapter = spinnerAdapter
        binding.stepTwoSchoolSp.setSelection(0) //첫 아이템을 스피너 초기값으로 설정해준다.
        if(signUpVM.getUniversityName()!="null"){ // 선택했던 대학교를 띄워준다.
            binding.stepTwoSchoolSp.setSelection(universityList.indexOf(signUpVM.getUniversityName()))
        }

        binding.stepTwoSchoolSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 축소된 스피너화면에 맞게 아이템 색상, 화살표 변경
                val image: ImageView? = view?.findViewById(R.id.university_arrow_iv)
                image?.setImageResource(R.drawable.ic_spinner_up)
                image?.visibility = View.VISIBLE

                if(universityList[position].length!=1){ // 자음을 하나짜리가 아니면
                    Log.d("cherry", universityList[position].toString())
                    universityList[universityList.size-1] = universityList[position] // 마지막 아이템은 현재 선택된 아이템 저장용
                }
                val textName: TextView? = view?.findViewById(R.id.spinner_university_text)
                textName?.text = universityList[universityList.size-1]
                university = textName?.text.toString()

                if (university == "자신의 학교를 선택해주세요") {
                    university = null
                    binding.stepTwoEmail2Et.setText("")
                }

                // 임의로 넣어놓은 부분!!
                // TODO: 나중에 학교 리스트 API 연결하기

                if (university == "가천대학교")
                    binding.stepTwoEmail2Et.setText("@gachon.ac.kr")
                else if (university == "나천대학교")
                    binding.stepTwoEmail2Et.setText("@nachon.ac.kr")
                else if (university == "다천대학교")
                    binding.stepTwoEmail2Et.setText("@dachon.ac.kr")
                else if (university == "라천대학교")
                    binding.stepTwoEmail2Et.setText("@rachon.ac.kr")
                else if (university == "마천대학교")
                    binding.stepTwoEmail2Et.setText("@machon.ac.kr")

            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun initTextChangedListener() {
        binding.stepTwoEmailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (university != null && binding.stepTwoEmailEt.text.isNotEmpty()) {
                    binding.stepTwoEmailCheckBtnX.visibility = View.GONE
                    binding.stepTwoEmailCheckBtnO.visibility = View.VISIBLE
                } else {
                    binding.stepTwoEmailCheckBtnX.visibility = View.VISIBLE
                    binding.stepTwoEmailCheckBtnO.visibility = View.GONE
                }

                isSendEmail = false
                checkingNext()
            }
        })

        binding.stepTwoEmail2Et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (binding.stepTwoEmail2Et.text.isNotEmpty() && binding.stepTwoEmailEt.text.isNotEmpty()) {
                    binding.stepTwoEmailCheckBtnX.visibility = View.GONE
                    binding.stepTwoEmailCheckBtnO.visibility = View.VISIBLE
                } else {
                    binding.stepTwoEmailCheckBtnX.visibility = View.VISIBLE
                    binding.stepTwoEmailCheckBtnO.visibility = View.GONE
                }

                isSendEmail = false
                checkingNext()
            }
        })
    }

    private fun initClickListener() {
        // 이메일 인증 전송 버튼
        binding.stepTwoEmailCheckBtnO.setOnClickListener {
            Log.d("email","인증번호 전송버튼 눌림")
            sendEmail()
        }

        //다음버튼
        binding.stepTwoNextBtn.setOnClickListener {
            signUpVM.setEmail(email)
            signUpVM.setUniversityName(university)

            val bundle = Bundle()
            bundle.putInt("time",time)
            val frag = StepThreeFragment()
            frag.arguments = bundle

            (context as SignUpActivity).supportFragmentManager.beginTransaction().replace(R.id.sign_up_vp, frag).addToBackStack("stepThree").commit()
        }
    }

    private fun sendEmail() {
        Log.d("email", "email")
        email = binding.stepTwoEmailEt.text.toString() + binding.stepTwoEmail2Et.text.toString()
        val uuid = getUuid().toString()
        val signUpEmailRequest = SignUpEmailRequest(email.toString(), university.toString(), uuid.toString())
        Log.d("email", email.toString()+"/"+university.toString()+"/"+uuid.toString())
        signUpService.signUpEmailSender(signUpEmailRequest)
    }

    override fun onSignUpEmailSuccess(message: String) {
        Log.d("email", "성공했습니다")
        //ToastMsgSingup 대신에 Utils에 CustomToastMsg만들어서 공용으로 쓰려고 하는데..! 괜찮은 것 같으면 주석 지우고 ToastMsgSignup 지워주면 될 것 같아용~
        //ToastMsgSignup.createToast((activity as SignUpActivity), "인증번호가 전송되었습니다.", "#8029ABE2")?.show()
        CustomToastMsg.createToast((activity as SignUpActivity), "인증번호가 전송되었습니다.", "#8029ABE2", 53)?.show()

        isSendEmail = true
        checkingNext()
        startTimer()
    }

    override fun onSignUpEmailFailure(code: Int, message: String)
    {   Log.d("email", "실패했습니다")
        Log.d("SIGNUP-RESPONSE", "실패했습니다")
        Log.d("SIGNUP-RESPONSE", "CODE = $code")


        when (code) {
            2803 -> ToastMsgSignup.createToast((activity as SignUpActivity), "유효하지 않은 인증번호입니다", "#80A8A8A8")?.show()
            2804 -> ToastMsgSignup.createToast((activity as SignUpActivity), "일일 최대 전송 횟수를 초과했습니다", "#80A8A8A8")?.show()
            2805 -> ToastMsgSignup.createToast((activity as SignUpActivity), "잠시 후에 다시 시도해주세요", "#80A8A8A8")?.show()
        }

    }

    private fun checkingNext(){
        if(isSendEmail){
            binding.stepTwoNextBtn.isEnabled = true;
            binding.stepTwoNextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.main))
            binding.stepTwoNextBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        }else{
            binding.stepTwoNextBtn.isEnabled = false;
            binding.stepTwoNextBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.gray_0))
            binding.stepTwoNextBtn.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_2))
        }
    }


}