package com.example.geeksasaeng.Profile

import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentProfileMyInfoUpdateBinding

class ProfileMyInfoUpdateFragment: BaseFragment<FragmentProfileMyInfoUpdateBinding>(FragmentProfileMyInfoUpdateBinding::inflate) {

    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener() {
        binding.profileMyInfoUpdateBackBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }
}