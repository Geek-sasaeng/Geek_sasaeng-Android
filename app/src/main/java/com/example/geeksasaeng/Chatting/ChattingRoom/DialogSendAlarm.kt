package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingDeliveryCompleteRequest
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingDeliveryCompleteView
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingService
import com.example.geeksasaeng.databinding.DialogSendAlarmLayoutBinding

class DialogSendAlarm: DialogFragment(){
    lateinit var binding: DialogSendAlarmLayoutBinding
    private lateinit var chattingService: ChattingService
    private lateinit var chattingDeliveryCompleteView: ChattingDeliveryCompleteView
    private var roomId : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        roomId = requireArguments().getString("roomId")
        binding = DialogSendAlarmLayoutBinding.inflate(inflater, container, false)
        initChattingService()
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.sendAlarmSendBtn.setOnClickListener {
            Log.d("deliveryAlarm", "배달완료 알림보내기 버튼 눌림 / "+ roomId)
            chattingService.sendDeliveryCompleteAlarm(ChattingDeliveryCompleteRequest(roomId!!)) //★ 매칭완료 알람보내기 api 호출
            this.dismiss()
        }

        binding.sendAlarmCancelBtn.setOnClickListener {
            this.dismiss()
        }
    }

    fun setChattingDeliveryCompleteView(chattingDeliveryCompleteView: ChattingDeliveryCompleteView){
        this.chattingDeliveryCompleteView = chattingDeliveryCompleteView
    }

    fun initChattingService(){
        chattingService = ChattingService() //서비스 객체 생성
        chattingService.setChattingDeliveryCompleteView(chattingDeliveryCompleteView)
    }

}