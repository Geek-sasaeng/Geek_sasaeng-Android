package com.example.geeksasaeng.Home.Delivery

import android.os.Parcelable
import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.Data.DeliveryData
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.FragmentLookPartyBinding

class LookPartyFragment: BaseFragment<FragmentLookPartyBinding>(FragmentLookPartyBinding::inflate) {
    lateinit var deliveryDataBinding: DeliveryData

    override fun initAfterBinding() {
        val deliveryData: Parcelable? = arguments?.getParcelable("DeliveryData")

        binding.lookTitle.setText(deliveryDataBinding.title)
        binding.lookTitle.setText(deliveryDataBinding.time)
        binding.lookMatchingNumber.setText(deliveryDataBinding.currentMember)
        binding.lookTitle.setText(deliveryDataBinding.totalMember)

        binding.lookPartyBackBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_frm, DeliveryFragment())?.commit()
        }
    }

    fun setDeliveryData(deliveryData: DeliveryData){
        deliveryDataBinding = deliveryData
    }
}