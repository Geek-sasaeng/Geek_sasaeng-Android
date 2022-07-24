package com.example.geeksasaeng.Signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.SignUpActivity
import com.example.geeksasaeng.databinding.FragmentStepOneBinding
import com.example.geeksasaeng.databinding.FragmentStepTwoBinding


class StepTwoFragment : Fragment() {

    lateinit var binding: FragmentStepTwoBinding

    var checkPassword: String? = ""
    var loginId: String? = ""
    var nickname: String? = ""
    var password: String? = ""
    var email: String? = ""
    var universityName: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentStepTwoBinding.inflate(inflater, container, false)

        checkPassword = arguments?.getString("checkPassword")
        loginId = arguments?.getString("loginId")
        nickname = arguments?.getString("nickname")
        password = arguments?.getString("password")

        initClickListener()

        return binding.root
    }

    private fun initClickListener() {
        binding.stepTwoNextBtn.setOnClickListener {
            val transaction: FragmentTransaction =
                (context as SignUpActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("checkPassword", checkPassword)
            bundle.putString("loginId", loginId)
            bundle.putString("nickname", nickname)
            bundle.putString("password", password)
            bundle.putString("email", binding.stepTwoEmailEt.text.toString() + "@" + binding.stepTwoEmail2InputEt.text.toString())
            bundle.putString("universityName", binding.stepTwoSchoolEt.text.toString())

            val stepThreeFragment = StepThreeFragment()
            stepThreeFragment.arguments = bundle

            Log.d("SignupData", bundle.toString())

            (context as SignUpActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.sign_up_vp, stepThreeFragment).commit()

            stepThreeFragment.arguments = bundle

            transaction.replace(R.id.sign_up_vp, stepThreeFragment)
            transaction.commit()
        }
    }
}