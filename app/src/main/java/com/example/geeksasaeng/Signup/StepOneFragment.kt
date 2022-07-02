package com.example.geeksasaeng.Signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.SignUpActivity
import com.example.geeksasaeng.databinding.FragmentDeliveryBinding
import com.example.geeksasaeng.databinding.FragmentStepOneBinding

class StepOneFragment: Fragment() {

    lateinit var binding: FragmentStepOneBinding

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentStepOneBinding.inflate(inflater, container, false)

        initClickListener()

        return binding.root
    }

    private fun initClickListener() {
        binding.stepOneNextBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("checkPassword", binding.stepOnePasswordCheckEt.text.toString())
            bundle.putString("loginId", binding.stepOneIdEt.text.toString())
            bundle.putString("nickname", binding.stepOneNickEt.text.toString())
            bundle.putString("password", binding.stepOnePasswordEt.text.toString())

            val stepTwoFragment = StepTwoFragment()
            stepTwoFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_vp, stepTwoFragment).commit()
        }
    }
}