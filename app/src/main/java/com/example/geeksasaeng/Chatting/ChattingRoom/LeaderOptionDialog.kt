package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingDeliveryCompleteView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.DialogChattingRoomOptionLeaderPopupBinding

class LeaderOptionDialog: DialogFragment(), ChattingDeliveryCompleteView {

    lateinit var binding: DialogChattingRoomOptionLeaderPopupBinding
    private var partyId : Int = 0
    private lateinit var roomId: String
    private var isMatchingFinish : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogChattingRoomOptionLeaderPopupBinding.inflate(inflater, container, false)

        partyId = requireArguments().getInt("partyId")
        roomId = requireArguments().getString("roomId").toString()
        isMatchingFinish = requireArguments().getBoolean("isMatchingFinish")
        Log.d("matching", isMatchingFinish.toString())

        initListener()
        initMatchingEndListener()
        dialog?.window?.setGravity(Gravity.TOP or Gravity.RIGHT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        dialog?.window?.setWindowAnimations(R.style.AnimationPopupStyle)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    private fun initListener(){
        // TODO 각 옵션 기능 넣기

        binding.dialogLeaderPopupOptionAlarmTv.setOnClickListener { //배달 완료 알림보내기
            val dialog = DialogSendAlarm()
            val bundle = Bundle()
            dialog.setChattingDeliveryCompleteView(this)
            bundle.putString("roomId", roomId)
            dialog.arguments = bundle
            dialog.show(parentFragmentManager, "CustomDialog")
        }

        Log.d("matching", isMatchingFinish.toString())
        if (isMatchingFinish){ //매칭 마감 여부에 따라 textView 색깔 다르게 해주기
            binding.dialogLeaderPopupOptionMatchingEndTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_2))
            binding.dialogLeaderPopupOptionMatchingEndTv.isEnabled = false
        }

        binding.dialogLeaderPopupOptionMatchingEndTv.setOnClickListener { //매칭마감하기 기능
            val warningDialog = DialogMatchingEnd()
            val bundle = Bundle()
            bundle.putInt("partyId", partyId)
            warningDialog.arguments = bundle
            warningDialog.show(parentFragmentManager, "MatchingEndWarningDialog")
        }

        binding.dialogLeaderPopupOptionUserExitTv.setOnClickListener { //강제 퇴장시키기

        }

        binding.dialogLeaderPopupOptionChattingExitTv.setOnClickListener{ //채팅 나가기
            val dialogChattingExit = DialogChattingExit()
            val bundle = Bundle()
            bundle.putString("roomId", roomId)
            bundle.putBoolean("isCheif",true)
            dialogChattingExit.arguments = bundle
            dialogChattingExit.show(parentFragmentManager, "DialogChattingExit")
        }
    }

    private fun initMatchingEndListener() {
        // TODO: 매칭 마감 확인
    }

    override fun chattingDeliveryCompleteSuccess() {
        Log.d("deliveryAlarm", "배달완료 알림보내기 성공")
        CustomToastMsg.createToast(requireContext(), "배달완료 알림 전송이 완료되었습니다", "#8029ABE2", 53)?.show()
    }

    override fun chattingDeliveryCompleteFailure(message: String) {
        Log.d("deliveryAlarm", "배달완료 알림보내기 실패")
        CustomToastMsg.createToast(requireContext(), message, "#8029ABE2", 53)?.show()
    }
}