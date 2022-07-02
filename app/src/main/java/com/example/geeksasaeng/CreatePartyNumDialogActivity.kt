package com.example.geeksasaeng

import android.os.Bundle
import android.util.Log
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.databinding.ActivityCreatePartyNumDialogBinding

class CreatePartyNumDialogActivity : BaseActivity<ActivityCreatePartyNumDialogBinding>(ActivityCreatePartyNumDialogBinding::inflate) {

    override fun initAfterBinding() {
        initNumberPicker()
        initClickListener()
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
    }


}