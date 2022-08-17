package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.DialogSendAlarmLayoutBinding
import com.example.geeksasaeng.databinding.DialogSignupPhoneSkipBinding

class DialogSendAlarm: DialogFragment() {
    lateinit var binding: DialogSendAlarmLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSendAlarmLayoutBinding.inflate(inflater, container, false)
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.sendAlarmSendBtn.setOnClickListener {
            //배달완료 알림 api where?
            this.dismiss()
            CustomToastMsg.createToast(requireContext(), "배달완료 알림 전송이 완료되었습니다", "#8029ABE2", 53)?.show()
        }

        binding.sendAlarmCancelBtn.setOnClickListener {
            this.dismiss()
        }
    }
}