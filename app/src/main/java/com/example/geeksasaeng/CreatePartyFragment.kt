package com.example.geeksasaeng

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.content.ContextCompat.startActivity
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.databinding.FragmentCreatePartyBinding
import com.example.geeksasaeng.databinding.FragmentStepOneBinding
import java.util.*


class CreatePartyFragment : BaseFragment<FragmentCreatePartyBinding>(FragmentCreatePartyBinding::inflate) {

    override fun initAfterBinding() {
        initClickListener()
    }

    override fun onResume() {
        super.onResume()
        //fragment에서 받는다.
        Log.d("dialog", arguments?.getString("DateDialog")+" found")
        if(arguments!=null){
            binding.createPartyDate2Tv.text = arguments?.getString("DateDialog") // 파티생성 프래그먼트의 날짜부분의 text값 바꿔주기
        }
    }

    private fun initClickListener(){

        binding.createPartyDate2Tv.setOnClickListener {
            Log.d("click","클릭됨")

            //파티생성하기 프레그먼트에서 다이얼로그로 전환
            val intent = Intent(context, CreatePartyDateDialogActivity::class.java)
            startActivity(intent)

        }
    }



}