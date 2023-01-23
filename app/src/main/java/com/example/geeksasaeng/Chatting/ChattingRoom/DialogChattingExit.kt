package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.*
import com.example.geeksasaeng.Home.Party.CreateParty.DialogLocation
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.DialogChattingExitBinding
import com.example.geeksasaeng.databinding.DialogChattingRoomForcedExitLeaderBinding

class DialogChattingExit: DialogFragment(),
    ChattingMemberLeavePartyView, ChattingMemberLeaveChatView,
    ChattingLeaderLeavePartyView, ChattingLeaderLeaveChatView {

    lateinit var binding: DialogChattingExitBinding
    private lateinit var chattingService: ChattingService
    private lateinit var roomId : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogChattingExitBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams

        roomId = arguments?.getString("roomId")!!

        initService()
        initLClickListener()
        return binding.root
    }

    private fun initService() {
        chattingService = ChattingService() //서비스 객체생성
        chattingService.setChattingMemberLeavePartyView(this)
        chattingService.setChattingMemberLeaveChatView(this)
        chattingService.setChattingLeaderLeavePartyView(this)
        chattingService.setChattingLeaderLeaveChatView(this)
    }

    private fun initLClickListener() {
        binding.chattingExitCancelBtn.setOnClickListener {
            this.dismiss()
            requireActivity().finish()
        }

        binding.chattingExitOkBtn.setOnClickListener {

            var isCheif = arguments!!.getBoolean("isCheif")

            if(isCheif){
                val chattingPartyLeaderLeaveChatRequest = ChattingPartyLeaderLeaveChatRequest(roomId)
                chattingService.getChattingPartyLeaderChatLeave(chattingPartyLeaderLeaveChatRequest)
            }else{
                val chattingPartyMemberLeaveChatRequest = ChattingPartyMemberLeaveChatRequest(roomId)
                chattingService.getChattingPartyMemberChatLeave(chattingPartyMemberLeaveChatRequest)
            }
        }
    }

    // 일반 유저 나가기 성공 /실패

    override fun chattingMemberLeavePartySuccess(result: String) {
        Log.d("exit","멤버 파티 나가기 성공" + result)
    }

    override fun chattingMemberLeavePartyFailure(code: Int, message: String) {
        Log.d("exit","멤버 파티 나가기 실패" + message)
    }

    override fun chattingMemberLeaveChatSuccess(result: ChattingPartyMemberLeaveChatResult) {
        Log.d("exit","멤버 채팅 나가기 성공" + result)
        val chattingPartyMemberLeavePartyRequest = ChattingPartyMemberLeavePartyRequest(roomId)
        chattingService.getChattingPartyMemberPartyLeave(chattingPartyMemberLeavePartyRequest)
        requireActivity().finish()
    }

    override fun chattingMemberLeaveChatFailure(code: Int, message: String) {
        Log.d("exit", "멤버 채팅 나가기 실패" + message)
    }

    // 방장 나가기 성공 /실패

    override fun chattingLeaderLeavePartySuccess(result: ChattingPartyLeaderLeavePartyResult) {
        Log.d("exit","방장 파티 나가기 성공"+ result)
    }

    override fun chattingLeaderLeavePartyFailure(code: Int, message: String) {
        Log.d("exit","방장 파티 나가기 실패" + message)
    }

    override fun chattingLeaderLeaveChatSuccess(result: ChattingPartyLeaderLeaveChatResult) {
        Log.d("exit","방장 채팅 나가기 성공" + result)
        val chattingPartyLeaderLeavePartyRequest = ChattingPartyLeaderLeavePartyRequest(getNickname(), roomId)
        chattingService.getChattingPartyLeaderPartyLeave(chattingPartyLeaderLeavePartyRequest)
        requireActivity().finish()
    }

    override fun chattingLeaderLeaveChatFailure(code: Int, message: String) {
        Log.d("exit","방장 채팅 나가기 실패" + message)
    }
}
