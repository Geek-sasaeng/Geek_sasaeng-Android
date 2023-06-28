package com.example.geeksasaeng.Chatting.ChattingRoom

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingService
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingUserProfileResult
import com.example.geeksasaeng.Chatting.ChattingRoom.Retrofit.ChattingUserProfileView
import com.example.geeksasaeng.Home.Party.ReportParty.PartyReportOptionActivity
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.DialogDeliveryOptionOtherPopupBinding
import com.example.geeksasaeng.databinding.FragmentChattingUserBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChattingUserBottomFragment: BottomSheetDialogFragment()  {

    lateinit var binding: FragmentChattingUserBottomBinding

    private lateinit var chattingService: ChattingService

    lateinit var chatRoomId: String
    lateinit var nickname: String
    lateinit var profileImgUrl: String
    lateinit var memberId: String
    lateinit var member: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChattingUserBottomBinding.inflate(inflater, container, false)

        chatRoomId = arguments?.getString("chatRoomId").toString()
        nickname = arguments?.getString("nickname").toString()
        profileImgUrl = arguments?.getString("profileImgUrl").toString()
        memberId = arguments?.getString("memberId").toString()
        member = arguments?.getString("member").toString()

        initData()
        initClickListener()

        return binding.root
    }

    private fun initData() {
        binding.userName.text = nickname
        context?.let {
            Glide.with(it)
                .load(profileImgUrl)
                .into(binding.itemMyChattingProfileIv)
        }
        binding.userPositionTv.text = member
    }

    private fun initClickListener() {
        binding.chattingUserBottomReport.setOnClickListener {
            //val intent = Intent(activity, MainActivity::class.java)
            //intent.putExtra("status", "chatReport")
            //intent.putExtra("memberId", memberId)
            //startActivity(intent)
            val intent = Intent(activity, ChattingUserReportActivity::class.java)
            intent.putExtra("memberId", memberId)
            startActivity(intent)
        }
    }
}