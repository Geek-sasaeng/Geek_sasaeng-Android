package com.example.geeksasaeng.Signup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.databinding.DialogSignupPhoneSkipBinding


class DialogSignUpPhoneSkip: DialogFragment() {

    lateinit var binding: DialogSignupPhoneSkipBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSignupPhoneSkipBinding.inflate(inflater, container, false)

        return binding.root
    }
}