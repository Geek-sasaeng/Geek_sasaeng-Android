package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingDeliveryComplicatedRequest
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingDeliveryComplicatedView
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingService
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpActivity
import com.example.geeksasaeng.Signup.Basic.StepFiveFragment
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.DialogSendAlarmLayoutBinding
import com.example.geeksasaeng.databinding.DialogSignupPhoneSkipBinding

class DialogSendAlarm: DialogFragment(), ChattingDeliveryComplicatedView{
    lateinit var binding: DialogSendAlarmLayoutBinding
    private lateinit var chattingService: ChattingService
    private var roomUuid : String? = null

    override fun onStart() {
        super.onStart()
        chattingService = ChattingService() //서비스 객체 생성
        chattingService.setChattingDeliveryComplicatedView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        roomUuid = requireArguments().getString("roomUuid")
        binding = DialogSendAlarmLayoutBinding.inflate(inflater, container, false)
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.sendAlarmSendBtn.setOnClickListener {
            Log.d("deliveryAlarm", "배달완료 알림보내기 버튼 눌림")
            chattingService.sendDeliveryComplicatedAlarm(ChattingDeliveryComplicatedRequest(roomUuid!!)) //★ 매칭완료 알람보내기 api 호출
        }

        binding.sendAlarmCancelBtn.setOnClickListener {
            this.dismiss()
        }
    }

    override fun chattingDeliveryComplicatedSuccess() {
        this.dismiss()
        Log.d("deliveryAlarm", "배달완료 알림보내기 성공")
        CustomToastMsg.createToast(requireContext(), "배달완료 알림 전송이 완료되었습니다", "#8029ABE2", 53)?.show()
    }

    override fun chattingDeliveryComplicatedFailure(message: String) {
        Log.d("deliveryAlarm", "배달완료 알림보내기 실패")
        CustomToastMsg.createToast(requireContext(), message, "#8029ABE2", 53)?.show()
    }
}