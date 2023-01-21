package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingDeliveryComplicatedView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.DialogChattingRoomOptionLeaderPopupBinding

class LeaderOptionDialog: DialogFragment(), ChattingDeliveryComplicatedView {

    lateinit var binding: DialogChattingRoomOptionLeaderPopupBinding
    private var partyId : Int = 0
    private lateinit var leaderOptionView: LeaderOptionView

    fun setLeaderOptionView(leaderOptionView: LeaderOptionView){
        this.leaderOptionView = leaderOptionView
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogChattingRoomOptionLeaderPopupBinding.inflate(inflater, container, false)
        partyId = requireArguments().getInt("partyId")
        Log.d("partyId", partyId.toString())
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
            dialog.setChattingDeliveryComplicatedView(this)
            bundle.putInt("partyId", partyId)
            dialog.arguments = bundle
            dialog.show(parentFragmentManager, "CustomDialog")
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
            //leaderOptionView.LeaderExistClick()
            val dialogChattingExit = DialogChattingExit()
            val bundle = Bundle()
            bundle.putBoolean("isCheif",true)
            dialogChattingExit.arguments = bundle
            dialogChattingExit.show(parentFragmentManager, "DialogChattingExit")
        }
    }

    private fun initMatchingEndListener() {
        // TODO: 매칭 마감 확인
    }

    override fun chattingDeliveryComplicatedSuccess() {
        Log.d("deliveryAlarm", "배달완료 알림보내기 성공")
        CustomToastMsg.createToast(requireContext(), "배달완료 알림 전송이 완료되었습니다", "#8029ABE2", 53)?.show()
    }

    override fun chattingDeliveryComplicatedFailure(message: String) {
        Log.d("deliveryAlarm", "배달완료 알림보내기 실패")
        CustomToastMsg.createToast(requireContext(), message, "#8029ABE2", 53)?.show()
    }
}