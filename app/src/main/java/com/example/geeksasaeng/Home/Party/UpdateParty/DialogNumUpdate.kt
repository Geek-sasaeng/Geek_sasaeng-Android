package com.example.geeksasaeng.Home.Party.UpdateParty

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Home.CreateParty.CreatePartyViewModel
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogNumLayoutBinding
import com.example.geeksasaeng.databinding.DialogNumUpdateLayoutBinding

class DialogNumUpdate: DialogFragment() {

    lateinit var binding: DialogNumUpdateLayoutBinding
    var numString = "2"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DialogNumUpdateLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initData()
        initNumberPicker()
        initClickListener()
        return binding.root
    }

    private fun initData(){
        //사용자가 입력해둔 정보 있으면, 그걸 default값으로 설정해주기 위함 (사용자가 입력해둔 정보 유무는 ViewModel에 저장되어 있는지 여부로 결정)

    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }

    private fun initNumberPicker(){
        val numArray = arrayOf("2명","3명","4명","5명","6명","7명","8명","9명","10명")
        binding.dialogNumUpdatePicker.minValue = 2
        binding.dialogNumUpdatePicker.maxValue = 10
        binding.dialogNumUpdatePicker.wrapSelectorWheel = false
        binding.dialogNumUpdatePicker.displayedValues = numArray
        binding.dialogNumUpdatePicker.value = numString.toInt() // numberPicker 초기값 설정하기
    }

    private fun initClickListener(){
        //완료버튼
        binding.dialogNumUpdateBtn.setOnClickListener {
            //다음 다이얼로그 띄우기
            val dialogCategory = DialogCategoryUpdate()
            dialogCategory.show(parentFragmentManager, "CustomDialog")
            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        //넘버픽커 값
        binding.dialogNumUpdatePicker.setOnValueChangedListener { picker, oldVal, newVal ->
            //oldVal 변동전 값, newVal이 변동 후의 값
            Log.d("test", "oldVal : ${oldVal}, newVal : $newVal")
            numString = newVal.toString()
        }

    }
}