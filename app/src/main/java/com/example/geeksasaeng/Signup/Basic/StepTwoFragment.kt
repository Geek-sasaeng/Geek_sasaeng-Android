package com.example.geeksasaeng.Signup.Basic

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.Data.EmailSend
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.DialogSignUpEmailCheck
import com.example.geeksasaeng.Signup.Email.EmailDataService
import com.example.geeksasaeng.databinding.FragmentStepTwoBinding
import java.util.*


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

        getUuid(context as SignUpActivity)
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

    private fun sendEmail() {
        val emailSendDataService = EmailDataService()
        // emailSendDataService.setEmailView(this)
        emailSendDataService.emailSend(EmailSend(binding.stepTwoEmailEt.text.toString() + "@" + binding.stepTwoEmail2Et.text.toString(), binding.stepTwoSchoolEt.text.toString(), uuid))

        Log.d("EMAIL-RESPONSE", "StepTwoFragment-sendEmail : Send Email Check")
    }
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

    fun getDevicesUUID(mContext: Context): String? {
        var tmDevice: String? = null
        var tmSerial: String? = null
        val androidId: String
        val deviceUuid: UUID
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceUuid = UUID.randomUUID() // UUID 대체코드
        } else {
            // 기존 UUID 로직
            val tm = mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tmDevice = "" + tm.deviceId
            tmSerial = "" + tm.simSerialNumber
            androidId = "" + Settings.Secure.getString(
                mContext.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            deviceUuid = UUID(
                androidId.hashCode().toLong(),
                tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode()
                    .toLong()
            )
        }
        return deviceUuid.toString()
    }
}