package com.example.geeksasaeng.Home.Party.LookParty

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.Home.Delivery.DeliveryFragment
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDataService
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDeleteView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogDeliveryDeleteBinding

class DialogPartyDelete: DialogFragment(), PartyDeleteView {

    lateinit var binding: DialogDeliveryDeleteBinding
    var partyId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDeliveryDeleteBinding.inflate(inflater, container, false)

        partyId = requireArguments().getInt("partyId")

        initListener()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 만들어줘야 둥근 테두리가 보인다.
        return binding.root
    }

    private fun initListener(){
        binding.deleteCancelBtn.setOnClickListener {
            //건너뛰기 취소
            this.dismiss()
        }
        
        binding.deliveryDeleteBtn.setOnClickListener {
            sendDeleteParty()
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: 테스트해보고 수정하기 (폰에 따라 크기 다르게 지정?!)
        val width = resources.getDimensionPixelSize(R.dimen.party_delete_popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.party_delete_popup_height)
        dialog?.window?.setLayout(width,height)
    }

    private fun sendDeleteParty() {
        val deletePartyService = PartyDataService()
        deletePartyService.setPartyDeleteView(this)
        deletePartyService.partyDeleteSender(partyId)
    }

    override fun partyDeleteViewSuccess(code: Int) {
        // Toast.makeText(activity, code.toString(), Toast.LENGTH_SHORT).show()
        this.dismiss()
        activity?.supportFragmentManager?.beginTransaction()!!.replace(R.id.main_frm, DeliveryFragment()).commit()
    }

    override fun partyDeleteViewFailure(message: String) {
        // Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}