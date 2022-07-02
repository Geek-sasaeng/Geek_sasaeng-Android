package com.example.geeksasaeng

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogDtLayoutBinding
import com.example.geeksasaeng.databinding.DialogNumLayoutBinding
import java.util.*

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

    private fun initNumberPicker(){
        binding.numDialogPicker.minValue = 1
        binding.numDialogPicker.maxValue = 10
        binding.numDialogPicker.wrapSelectorWheel = false

    }

    private fun initClickListener(){
        binding.numDialogNextBtn.setOnClickListener {

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