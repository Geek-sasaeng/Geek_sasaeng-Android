package com.example.geeksasaeng.Signup.Basic

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.DialogSignUpEmailCheck
import com.example.geeksasaeng.databinding.FragmentStepTwoBinding


class StepTwoFragment : BaseFragment<FragmentStepTwoBinding>(FragmentStepTwoBinding::inflate) {

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var university: String? = ""

    var uuid: String? = null

    private val progressVM: ProgressViewModel by activityViewModels()

    override fun initAfterBinding() {
        progressVM.increase()

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

            email = binding.stepTwoEmailEt.text.toString() + "@" + binding.stepTwoEmail2Et.text.toString()
            university = binding.stepTwoSchoolEt.text.toString()

            bundle.putString("email", email)
            bundle.putString("universityName", university)

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

//    private fun sendEmail() {
//        val emailSendDataService = EmailDataService()
//        emailSendDataService.setEmailView(this@StepTwoFragment)
//        emailSendDataService.emailSend(EmailSend(binding.stepTwoEmailEt.text.toString() + "@" + binding.stepTwoEmail2Et.text.toString(), binding.stepTwoSchoolEt.text.toString(), uuid))
//
//        Log.d("EMAIL-RESPONSE", "StepTwoFragment-sendEmail : Send Email Check")
//    }
//
//    //인증번호 문자 보내는 작업
//    private fun sendSms(){
//        phoneNumber = binding.stepFourPhoneEt.text.toString() //사용자가 입력한 휴대폰 번호 가져오기
//        val signUpSmsRequest= SignUpSmsRequest(phoneNumber!!, getUuid().toString())
//        Log.d("sms",phoneNumber.toString()+"/"+ getUuid().toString()+"으로 문자 보냄")
//        signUpService.signUpSmsSender(signUpSmsRequest) //★인증문자보내기
//    }































//
//    override fun onEmailSendSuccess(code : Int , result: LoginResult) {
////        when(code) {
////            1000 -> {
////                if (binding.loginAutologinCb.isChecked && binding.loginIdEt.text.isNotEmpty() && binding.loginPwdEt.text.isNotEmpty()) {
////                    Log.d("LOGIN-RESPONSE", "IF CHECK")
////                    removeSP()
////                    saveSP(result.jwt)
////                } else if (binding.loginAutologinCb.isChecked == false) {
////                    removeSP()
////                }
////                finish()
////                changeActivity(MainActivity::class.java)
////            } else -> {
////            Toast.makeText(this, "LoginActivity-onLoginSuccess : Fail", Toast.LENGTH_SHORT).show()
////        }
////        }
//    }
//
//    override fun onEmailSendFailure(code: Int, message: String) {
////        Log.d("LOGIN-RESPONSE", "LoginActivity-onLoginFailure : Fail Code = $code")
////
////        if (code == 2011) showToast(message)
////        else if (code == 2012) showToast(message)
////        else if (code == 2400) showToast(message)
//    }

//    @SuppressLint("MissingPermission")
//    private fun getUuid(mContext: Context): String? {
//        val tm = mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//
//        val tmDevice: String = "" + tm.deviceId
//        val tmSerial: String = "" + tm.simSerialNumber
//        val androidId: String = "" + Settings.Secure.getString(
//            activity?.contentResolver,
//            Settings.Secure.ANDROID_ID
//        )
//
//        val deviceUuid = UUID(
//            androidId.hashCode().toLong(),
//            tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode()
//                .toLong()
//        )
//
//        Log.d("EMAIL-RESPONSE", "StepTwoFragment = " + deviceUuid.toString())
//
//        return deviceUuid.toString()
//    }

//    //인증번호 문자 보내는 작업
//    private fun sendSms(){
//        email = binding.stepTwoEmailEt.text.toString() + "@" + binding.stepTwoEmail2Et.text.toString() //사용자가 입력한 휴대폰 번호 가져오기
//
//        if (getUuid() == null){ //uuid가 존재하지 않으면,
//            val uuid = UUID.randomUUID().toString() //uuid 생성
//            saveUuid(uuid) // sharedpref에 저장
//        }
//
//        val emailSendRequest = EmailSend(email!!, getUuid().toString())
//        Log.d("sms",email.toString() + "/" + getUuid().toString()+"으로 문자 보냄")
//        signUpService.signUpSmsSender(signUpSmsRequest) //★인증문자보내기
//    }
}