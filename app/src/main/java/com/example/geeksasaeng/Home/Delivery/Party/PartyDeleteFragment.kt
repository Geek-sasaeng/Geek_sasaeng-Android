package com.example.geeksasaeng.Home.Delivery.Party

import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyDeleteBinding
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyUpdateBinding

class PartyDeleteFragment: BaseFragment<FragmentDeliveryPartyDeleteBinding>(FragmentDeliveryPartyDeleteBinding::inflate) {

    override fun initAfterBinding() {
        
    }

    override fun onStop() {
        super.onStop()
        (context as MainActivity).supportFragmentManager.beginTransaction().remove(this).commit();
        (context as MainActivity).supportFragmentManager.popBackStack();
    }
}