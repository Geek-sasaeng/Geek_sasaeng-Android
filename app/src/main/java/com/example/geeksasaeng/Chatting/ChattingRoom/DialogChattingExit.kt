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
import com.example.geeksasaeng.Home.Party.CreateParty.DialogLocation
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogChattingExitBinding
import com.example.geeksasaeng.databinding.DialogChattingRoomForcedExitLeaderBinding

class DialogChattingExit: DialogFragment() {
    lateinit var binding: DialogChattingExitBinding

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

        initLClickListener()
        return binding.root
    }

    private fun initLClickListener() {
        binding.chattingExitCancelBtn.setOnClickListener {
            this.dismiss()
        }

        binding.chattingExitOkBtn.setOnClickListener {

            var leader = arguments!!.getBoolean("leader")
            Log.d("exitDialog", leader.toString())

            if(leader){
                var leaderOptionView = activity as LeaderOptionView
                leaderOptionView.LeaderExistClick()
            }else{
                var memberOptionView = activity as MemberOptionView
                memberOptionView.MemberExistClick()
            }
        }
    }
}
