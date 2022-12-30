package com.example.geeksasaeng.Chatting.ChattingRoom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogForcedExitBinding


class DialogForcedExit: DialogFragment() {

    lateinit var binding: DialogForcedExitBinding

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.matching_end_popup_width) //256dp
        //val height = resources.getDimensionPixelSize(R.dimen.matching_end_popup_height) //250dp
        // TODO: 흠 얘는 안에 RV가 있어서 좀 height가 동적으로 변해야하는데 어떻게 설정해주지
        //dialog?.window?.setLayout(width,height)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogForcedExitBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

}


