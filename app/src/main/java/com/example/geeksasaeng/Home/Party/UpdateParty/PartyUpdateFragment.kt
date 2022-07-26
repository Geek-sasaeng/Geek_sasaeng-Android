package com.example.geeksasaeng.Home.Party.UpdateParty

import android.util.Log
import com.example.geeksasaeng.Home.Party.UpdateParty.Retrofit.UpdatePartyView
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyUpdateBinding


class PartyUpdateFragment: BaseFragment<FragmentDeliveryPartyUpdateBinding>(FragmentDeliveryPartyUpdateBinding::inflate), UpdatePartyView {

    override fun initAfterBinding() {

    }

    override fun onUpdatePartySuccess() {
        //수정 성공하면, 파티보기로 이동하면서 수정이 완료되었습니다 토스트 메세지 띄워줘야해
    }

    override fun onUpdatePartyFailure(message: String) {
        Log.d("updateParty", message)
    }
}