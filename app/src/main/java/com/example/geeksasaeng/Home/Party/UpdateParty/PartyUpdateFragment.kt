package com.example.geeksasaeng.Home.Party.UpdateParty

import android.util.Log
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyUpdateBinding


class PartyUpdateFragment: BaseFragment<FragmentDeliveryPartyUpdateBinding>(FragmentDeliveryPartyUpdateBinding::inflate) {

    var authorStatus: Boolean? = null
    var chief: String? = null
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

    override fun initAfterBinding() {
        // 이 부분이 받은 데이터 부분이야!
        authorStatus = requireArguments().getBoolean("partyId")
        chief = requireArguments().getString("chief")
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
    }
}