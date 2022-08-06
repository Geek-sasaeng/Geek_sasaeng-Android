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
import com.example.geeksasaeng.databinding.DialogAccountNumberLayoutBinding

class DialogAccountNumber : DialogFragment() {
    lateinit var binding: DialogAccountNumberLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogAccountNumberLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initClickListener()
        return binding.root
    }

    private fun initClickListener(){

    }
}