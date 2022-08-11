package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogChattingRoomOptionLeaderPopupBinding

class ChattingLeaderOptionDialog: DialogFragment() {
    lateinit var binding: DialogChattingRoomOptionLeaderPopupBinding

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
    }
}