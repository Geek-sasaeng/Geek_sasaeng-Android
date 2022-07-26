package com.example.geeksasaeng.Home.Party

import android.os.Parcelable
import androidx.fragment.app.FragmentManager
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.databinding.FragmentLookPartyBinding

class LookPartyFragment: BaseFragment<FragmentLookPartyBinding>(FragmentLookPartyBinding::inflate) {
    override fun initAfterBinding() {
        initClickListener()

        // 파티 수정하기, 신고하기 Stack에서 제거
        (context as MainActivity).supportFragmentManager.popBackStack("partyUpdate", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        (context as MainActivity).supportFragmentManager.popBackStack("partyReport", FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val deliveryData: Parcelable? = arguments?.getParcelable("DeliveryData")
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