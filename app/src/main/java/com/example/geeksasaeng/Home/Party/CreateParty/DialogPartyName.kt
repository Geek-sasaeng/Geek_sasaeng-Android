package com.example.geeksasaeng.Home.Party.CreateParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogPartyNameLayoutBinding

class DialogPartyName : DialogFragment() {
    lateinit var binding: DialogPartyNameLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPartyNameLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initClickListener()
        return binding.root
    }

    private fun initClickListener(){

    }

}