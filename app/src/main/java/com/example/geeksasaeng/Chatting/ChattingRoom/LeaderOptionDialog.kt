package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingDeliveryComplicatedView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.DialogChattingRoomOptionLeaderPopupBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LeaderOptionDialog: DialogFragment(), ChattingDeliveryComplicatedView {

    lateinit var binding: DialogChattingRoomOptionLeaderPopupBinding
    private var roomUuid : String? = null
    private lateinit var leaderOptionView: LeaderOptionView
    private val db = Firebase.firestore

    fun setLeaderOptionView(leaderOptionView: LeaderOptionView){
        this.leaderOptionView = leaderOptionView
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogChattingRoomOptionLeaderPopupBinding.inflate(inflater, container, false)
        roomUuid = requireArguments().getString("roomUuid")
        Log.d("roomUuid", roomUuid.toString())
        initListener()
        initMatchingEndListener()
        dialog?.window?.setGravity(Gravity.TOP or Gravity.RIGHT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        dialog?.window?.setWindowAnimations(R.style.AnimationPopupStyle)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.chatting_room_option_leader_width)
        val height = resources.getDimensionPixelSize(R.dimen.chatting_room_option_leader_height)
        dialog?.window?.setLayout(width,height)
    }

    private fun initListener(){
        // TODO 각 옵션 기능 넣기

        binding.dialogLeaderPopupOptionAlarmTv.setOnClickListener { //배달 완료 알림보내기
            val dialog = DialogSendAlarm()
            val bundle = Bundle()
            dialog.setChattingDeliveryComplicatedView(this)
            bundle.putString("roomUuid", roomUuid)
            dialog.arguments = bundle
            dialog.show(parentFragmentManager, "CustomDialog")
        }

        binding.dialogLeaderPopupOptionMatchingEndTv.setOnClickListener { //매칭마감하기 기능
            val warningDialog = DialogMatchingEnd()
            val bundle = Bundle()
            bundle.putString("roomUuid", roomUuid)
            warningDialog.arguments = bundle
            warningDialog.show(parentFragmentManager, "MatchingEndWarningDialog")
        }

        binding.dialogLeaderPopupOptionUserExitTv.setOnClickListener { //강제 퇴장시키기

        }

        binding.dialogLeaderPopupOptionChattingExitTv.setOnClickListener{ //채팅 나가기
            leaderOptionView.LeaderExistClick()
        }
    }

    private fun initMatchingEndListener() {
        db.collection("Rooms").document(roomUuid!!).collection("Messages").addSnapshotListener { snapshots, _ ->
            for (dc in snapshots?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    // 지우지마세요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 일부러 남겨놓은 부분입니다!!!!!!!!!!!!!!!!!!! -루나
                    // if (dc.document["content"] == getString(R.string.chatting_matching_end)) {
                    if (dc.document["content"] == "모든 파티원이 입장을 마쳤습니다 !\n파티장을 주문을, 파티원들은 송금을 진행해주세요") {
                        binding.dialogLeaderPopupOptionMatchingEndTv.setTextColor(Color.parseColor("#A8A8A8"))
                        binding.dialogLeaderPopupOptionMatchingEndTv.isEnabled = false
                    }
                }
            }
        }
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