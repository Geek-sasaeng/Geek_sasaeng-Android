package com.example.geeksasaeng.Home.Delivery.Party

import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyReportBinding
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyUpdateBinding

class PartyReportFragment: BaseFragment<FragmentDeliveryPartyReportBinding>(FragmentDeliveryPartyReportBinding::inflate) {

    override fun initAfterBinding() {

    }

    override fun onStop() {
        super.onStop()
        (context as MainActivity).supportFragmentManager.beginTransaction().remove(this).commit();
        (context as MainActivity).supportFragmentManager.popBackStack();
    }
}