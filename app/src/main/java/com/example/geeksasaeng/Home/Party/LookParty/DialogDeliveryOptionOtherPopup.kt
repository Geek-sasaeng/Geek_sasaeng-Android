package com.example.geeksasaeng.Home.Party.LookParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Home.Party.ReportParty.PartyReportFragment
import com.example.geeksasaeng.Home.Party.UpdateParty.PartyUpdateFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogDeliveryOptionOtherPopupBinding

class DialogDeliveryOptionOtherPopup: DialogFragment() {

    lateinit var binding: DialogDeliveryOptionOtherPopupBinding
    var reportedDeliveryPartyId: Int = 0
    var reportedMemberId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDeliveryOptionOtherPopupBinding.inflate(inflater, container, false)

        initListener()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        dialog?.window?.setGravity(Gravity.TOP or Gravity.RIGHT)
        dialog?.window?.setWindowAnimations(R.style.AnimationPopupStyle)

        reportedMemberId = requireArguments().getInt("reportedMemberId")
        reportedDeliveryPartyId = requireArguments().getInt("reportedDeliveryPartyId")

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.party_popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.party_other_popup_height)
        dialog?.window?.setLayout(width, height)
    }

    private fun initListener() {
        binding.deliveryOptionReportTv.setOnClickListener {
            // 신고하기
            this.dismiss()

            val transaction = (context as MainActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putInt("reportedDeliveryPartyId", reportedDeliveryPartyId)
            bundle.putInt("reportedMemberId", reportedMemberId)

            val partyReportFragment = PartyReportFragment()
            partyReportFragment.arguments = bundle

            transaction.addToBackStack("partyReport").replace(R.id.main_frm, partyReportFragment).commit()
        }
    }
}