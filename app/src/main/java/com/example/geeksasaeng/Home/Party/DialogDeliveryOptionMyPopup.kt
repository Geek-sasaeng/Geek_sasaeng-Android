package com.example.geeksasaeng.Home.Party

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDataService
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDeleteView
import com.example.geeksasaeng.Home.Party.UpdateParty.DialogDtUpdate
import com.example.geeksasaeng.Home.Party.UpdateParty.PartyUpdateFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogDeliveryOptionMyPopupBinding

class DialogDeliveryOptionMyPopup: DialogFragment() {

    lateinit var binding: DialogDeliveryOptionMyPopupBinding
    var authorStatus: Boolean? = null
    var chief: String? = null
    // var chiefId: Int = 0
    var chiefProfileImgUrl: String? = null
    var content: String? = null
    var currentMatching: Int = 0
    var dormitory: Int = 0
    var foodCategory: String? = null
    var hashTag: Boolean? = null
    var partyId: Int = 0
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var matchingStatus: String? = null
    var maxMatching: Int = 0
    var orderTime: String? = null
    var storeUrl: String? = null
    var title: String? = null
    var updatedAt: String? = null

    private var UpdateClickListener: PopUpdateClickListener? =null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDeliveryOptionMyPopupBinding.inflate(inflater, container, false)

        authorStatus = requireArguments().getBoolean("partyId")
        chief = requireArguments().getString("chief")
        // chiefId = requireArguments().getInt("chiefId")
        chiefProfileImgUrl = requireArguments().getString("chiefProfileImgUrl")
        content = requireArguments().getString("content")
        currentMatching = requireArguments().getInt("currentMatching")
        dormitory = requireArguments().getInt("dormitory")
        foodCategory = requireArguments().getString("foodCategory")
        hashTag = requireArguments().getBoolean("hashTag")
        partyId = requireArguments().getInt("partyId")
        latitude = requireArguments().getDouble("latitude", latitude)
        longitude = requireArguments().getDouble("longitude", longitude)
        matchingStatus = requireArguments().getString("matchingStatus")
        maxMatching = requireArguments().getInt("maxMatching")
        orderTime = requireArguments().getString("orderTime")
        storeUrl = requireArguments().getString("storeUrl")
        title = requireArguments().getString("title")
        updatedAt = requireArguments().getString("updatedAt")

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

    interface PopUpdateClickListener{
        //TODO: 뷰모델 이용하면서 사실 여기서 매개변수로 안넘겨줘도 ACTIVITY에서 값 알 수 있어..
        //TODO: 근데 이걸 하는 이유는 정보 갱신을 위함.
        fun onPopUpdateClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        UpdateClickListener = requireParentFragment() as PopUpdateClickListener
    }

    override fun onDetach() {
        super.onDetach()
        UpdateClickListener = null
    }

    private fun initListener() {
        binding.deliveryOptionUpdateTv.setOnClickListener {
            // 수정하기
            UpdateClickListener?.onPopUpdateClicked()
            this.dismiss()

            val transaction: FragmentTransaction = (context as MainActivity).supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putBoolean("authorStatus", authorStatus!!)
            bundle.putString("chief", chief)
            bundle.putString("chiefProfileImgUrl", chiefProfileImgUrl)
            bundle.putString("content", content)
            bundle.putInt("currentMatching", currentMatching)
            bundle.putInt("dormitory", dormitory)
            bundle.putString("foodCategory", foodCategory)
            bundle.putBoolean("hashTag", hashTag!!)
            bundle.putInt("partyId", id)
            bundle.putDouble("latitude", latitude)
            bundle.putDouble("longitude", longitude)
            bundle.putString("matchingStatus", matchingStatus)
            bundle.putInt("maxMatching", maxMatching)
            bundle.putString("orderTime", orderTime)
            bundle.putString("storeUrl", storeUrl)
            bundle.putString("title", title)
            bundle.putString("updatedAt", updatedAt)

            val partyUpdateFragment = PartyUpdateFragment()
            partyUpdateFragment.arguments = bundle

            transaction.addToBackStack("partyUpdate").replace(R.id.main_frm, partyUpdateFragment).commit()
        }

        binding.deliveryOptionDeleteTv.setOnClickListener {
            // 삭제하기 팝업으로 이동
            this.dismiss()

            val bundle = Bundle()
            bundle.putInt("partyId", partyId!!)

            var dialogFragment = DialogPartyDelete()
            dialogFragment.arguments = bundle
            dialogFragment.show(childFragmentManager, "DialogPartyDelete")
        }
    }
}