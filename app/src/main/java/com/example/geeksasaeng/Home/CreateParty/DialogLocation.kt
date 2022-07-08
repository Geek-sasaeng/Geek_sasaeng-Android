package com.example.geeksasaeng.Home.CreateParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogLocationLayoutBinding


class DialogLocation: DialogFragment() {

    lateinit var binding: DialogLocationLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DialogLocationLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initClickListener()
        return binding.root
    }

    private fun initClickListener(){
        binding.locationDialogNextBtn.setOnClickListener {
            //마지막 페이지이므로 그냥 종료
            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        binding.locationDialogBackBtn.setOnClickListener {

            //이전 다이얼로그 실행
            val dialogCategory = DialogCategory()
            dialogCategory.show(parentFragmentManager, "CustomDialog")

            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }
    }

}