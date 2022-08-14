package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Chatting.Retrofit.ChattingDataService
import com.example.geeksasaeng.Chatting.Retrofit.MatchingEndRequest
import com.example.geeksasaeng.Chatting.Retrofit.MatchingEndView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogChattingRoomOptionLeaderPopupBinding

class ChattingLeaderOptionDialog: DialogFragment() {
    lateinit var binding: DialogChattingRoomOptionLeaderPopupBinding
    private var roomUuid : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogChattingRoomOptionLeaderPopupBinding.inflate(inflater, container, false)
        initListener()
        dialog?.window?.setGravity(Gravity.TOP or Gravity.RIGHT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        dialog?.window?.setWindowAnimations(R.style.AnimationPopupStyle)

        roomUuid = requireArguments().getString("roomUuid")

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.party_request_complete_width)
        val height = resources.getDimensionPixelSize(R.dimen.party_request_complete_height)
        dialog?.window?.setLayout(width,height)
    }

    private fun initListener(){
        // TODO 각 옵션 기능 넣기
        binding.dialogLeaderPopupOptionLookMenuTv.setOnClickListener { //메뉴판 보기

        }

        binding.dialogLeaderPopupOptionAlarmTv.setOnClickListener { //배달 완료 알림보내기

        }

        binding.dialogLeaderPopupOptionMatchingEndTv.setOnClickListener { //매칭마감하기 기능
            val warningDialog = DialogMatchingEnd()
            val bundle = Bundle()
            bundle.putString("roomUuid", roomUuid) //TODO: 매칭마감 pathvariable에 roomUuid를 넣어주는 것으로 바뀔예정
            warningDialog.arguments = bundle
            warningDialog.show(parentFragmentManager, "MatchingEndWarningDialog")
        }

        binding.dialogLeaderPopupOptionUserExitTv.setOnClickListener { //강제 퇴장시키기

        }

        binding.dialogLeaderPopupOptionChattingEndTv.setOnClickListener { //채팅 종료하기

        }

        binding.dialogLeaderPopupOptionChattingExitTv.setOnClickListener{ //채팅 나가기

        }
    }

}