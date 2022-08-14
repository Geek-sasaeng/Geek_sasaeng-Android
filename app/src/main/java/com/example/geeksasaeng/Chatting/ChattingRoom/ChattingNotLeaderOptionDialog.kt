package com.example.geeksasaeng.Chatting.ChattingRoom

import android.R
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogChattingRoomOptionNotLeaderPopupBinding


class ChattingNotLeaderOptionDialog: DialogFragment() {
    lateinit var binding: DialogChattingRoomOptionNotLeaderPopupBinding
    lateinit var notLeaderOptionView: NotLeaderOptionView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogChattingRoomOptionNotLeaderPopupBinding.inflate(inflater, container, false)
        dialog?.window?.setGravity(Gravity.TOP or Gravity.RIGHT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        dialog?.window?.setWindowAnimations(com.example.geeksasaeng.R.style.AnimationPopupStyle)
        initListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val width =
            resources.getDimensionPixelSize(com.example.geeksasaeng.R.dimen.chatting_room_option_not_leader_width)
        val height = resources.getDimensionPixelSize(com.example.geeksasaeng.R.dimen.chatting_room_option_not_leader_height)
        dialog?.window?.setLayout(width, height)
    }

    private fun initListener() {
        initExistClickListener()
    }

    private fun initExistClickListener(){
        binding.dialogNotLeaderPopupOptionExitTv.setOnClickListener {
            notLeaderOptionView.NotLeaderExistClick()
        }
    }
}
