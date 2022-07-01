package com.example.geeksasaeng.Signup

import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.databinding.FragmentSignupPhoneBinding

class SignupPhoneFragment: BaseFragment<FragmentSignupPhoneBinding> (FragmentSignupPhoneBinding::inflate) {

    override fun initAfterBinding() {
        binding.signupPhoneSkipBtn.setOnClickListener {
            // Fragment 이동
        }
    }
}