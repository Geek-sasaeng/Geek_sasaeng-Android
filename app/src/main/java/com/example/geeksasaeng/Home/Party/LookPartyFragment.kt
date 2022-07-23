package com.example.geeksasaeng.Home.Party

import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.databinding.FragmentLookPartyBinding

class LookPartyFragment: BaseFragment<FragmentLookPartyBinding>(FragmentLookPartyBinding::inflate) {
    override fun initAfterBinding() {
        initClickListener()
    }

    private fun initClickListener(){
        binding.lookPartyBackBtn.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().remove(this).commit();
            (context as MainActivity).supportFragmentManager.popBackStack();
        }

        binding.lookPartyOptionBtn.setOnClickListener {
            val dialog = DialogDeliveryOptionPopup()
            dialog.show(parentFragmentManager, "DeliveryPartyOption")
        }
    }
}