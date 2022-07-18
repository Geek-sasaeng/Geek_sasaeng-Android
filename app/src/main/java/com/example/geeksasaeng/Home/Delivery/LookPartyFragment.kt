package com.example.geeksasaeng.Home.Delivery

import android.os.Parcelable
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Home.HomeFragment
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.FragmentLookPartyBinding

class LookPartyFragment: BaseFragment<FragmentLookPartyBinding>(FragmentLookPartyBinding::inflate) {
    override fun initAfterBinding() {
        val deliveryData: Parcelable? = arguments?.getParcelable("DeliveryData")
        initClickListener()
    }

    private fun initClickListener(){
        binding.lookPartyBackBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_frm, HomeFragment())?.commit()
        }
    }
}