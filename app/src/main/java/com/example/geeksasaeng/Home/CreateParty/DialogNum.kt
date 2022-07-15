package com.example.geeksasaeng.Home.CreateParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogNumLayoutBinding

class DialogNum: DialogFragment() {

    lateinit var binding: DialogNumLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DialogNumLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initNumberPicker()
        initClickListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }


    private fun initNumberPicker(){
        val numArray = arrayOf("1명","2명","3명","4명","5명","6명","7명","8명","9명","10명")
        binding.numDialogPicker.minValue = 1
        binding.numDialogPicker.maxValue = 10
        binding.numDialogPicker.wrapSelectorWheel = false
        binding.numDialogPicker.displayedValues = numArray

    }

    private fun initClickListener(){
        binding.numDialogNextBtn.setOnClickListener {
            //다음 다이얼로그 띄우기
            val dialogCategory = DialogCategory()
            dialogCategory.show(parentFragmentManager, "CustomDialog")
            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        binding.numDialogPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            //oldVal 변동전 값, newVal이 변동 후의 값
            Log.d("test", "oldVal : ${oldVal}, newVal : $newVal")
            var num = newVal
        }

        binding.numDialogBackBtn.setOnClickListener {

            //이전 다이얼로그 실행
            val dialogDt = DialogDt()
            dialogDt.show(parentFragmentManager, "CustomDialog")

            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }
    }
}