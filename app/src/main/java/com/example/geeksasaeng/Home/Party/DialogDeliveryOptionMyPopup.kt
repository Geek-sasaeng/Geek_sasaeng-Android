package com.example.geeksasaeng.Home.Party

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDataService
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDeleteView
import com.example.geeksasaeng.Home.Party.UpdateParty.PartyUpdateFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogDeliveryOptionMyPopupBinding

class DialogDeliveryOptionMyPopup: DialogFragment() {

    lateinit var binding: DialogDeliveryOptionMyPopupBinding
    var partyId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDeliveryOptionMyPopupBinding.inflate(inflater, container, false)

        partyId = requireArguments().getInt("partyId")

        initListener()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        dialog?.window?.setGravity(Gravity.TOP or Gravity.RIGHT)
        dialog?.window?.setWindowAnimations(R.style.AnimationPopupStyle)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.party_popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.party_my_popup_height)
        dialog?.window?.setLayout(width, height)
    }

    private fun initListener() {
        binding.deliveryOptionUpdateTv.setOnClickListener {
            // 수정하기
            this.dismiss()
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, PartyUpdateFragment()).addToBackStack("partyUpdate").commit()
        }

        binding.deliveryOptionDeleteTv.setOnClickListener {
            // 삭제하기 팝업으로 이동
            this.dismiss()

            val bundle = Bundle()
            bundle.putInt("partyId", partyId!!)

            var dialogFragment = DialogPartyDelete()
            dialogFragment.arguments = bundle
            dialogFragment.show(parentFragmentManager, "DialogPartyDelete")
        }
    }
}