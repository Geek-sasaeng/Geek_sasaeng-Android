package com.example.geeksasaeng.Home.Party

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Home.Party.ReportParty.PartyReportFragment
import com.example.geeksasaeng.Home.Party.UpdateParty.PartyUpdateFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogDeliveryOptionPopupBinding

class DialogDeliveryOptionPopup: DialogFragment() {

    lateinit var binding: DialogDeliveryOptionPopupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDeliveryOptionPopupBinding.inflate(inflater, container, false)
        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        dialog?.window?.setGravity(Gravity.TOP or Gravity.RIGHT)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.party_popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.party_popup_height)
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
            // 삭제하기
            this.dismiss()

            val dialog = DialogPartyDelete()
            dialog.show(parentFragmentManager, "DialogPartyDelete")
        }

        binding.deliveryOptionReportTv.setOnClickListener {
            // 신고하기
            this.dismiss()
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, PartyReportFragment()).addToBackStack("partyReport").commit()
        }
    }
}