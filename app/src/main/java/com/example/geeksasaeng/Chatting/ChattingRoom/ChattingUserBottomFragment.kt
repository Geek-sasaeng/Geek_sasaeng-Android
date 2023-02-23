package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.DialogDeliveryOptionOtherPopupBinding
import com.example.geeksasaeng.databinding.FragmentChattingUserBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChattingUserBottomFragment(): BottomSheetDialogFragment()  {

    lateinit var binding: FragmentChattingUserBottomBinding

    lateinit var nickname: String
    lateinit var profileImgUrl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChattingUserBottomBinding.inflate(inflater, container, false)
        initData()
        return binding.root
    }

    private fun initData() {
        nickname = arguments?.getString("nickname").toString()
        profileImgUrl = arguments?.getString("profileImgUrl").toString()

        binding.userName.text = nickname
        context?.let {
            Glide.with(it)
                .load(profileImgUrl)
                .into(binding.itemMyChattingProfileIv)
        }
    }
}