package com.example.geeksasaeng.Home.Party.Retrofit

import android.util.Log
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryRetrofitInterfaces
import com.example.geeksasaeng.Utils.ApplicationClass
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PartyDataService {

    //뷰 객체 생성
    private lateinit var partyDetailView: PartyDetailView

    //setView
    fun setPartyDetailView(partyDetailView: PartyDetailView){
        this.partyDetailView = partyDetailView
    }

    //파티보기 상세조회
    fun partyDetailSender(partyId: Int) {
        val partyDetailService = NetworkModule.getInstance()?.create(PartyRetrofitInterface::class.java)
        partyDetailService?.getDeliveryPartyDetail(partyId)?.enqueue(object : Callback<PartyDetailResponse> {
            override fun onResponse(call: Call<PartyDetailResponse>, response: Response<PartyDetailResponse>) {
                Log.d("PARTY-DETAIL-RESPONSE", "PartyDataService-onResponse : response.code = " + response.code())
                Log.d("PARTY-DETAIL-RESPONSE", "PartyDataService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val partyDetailResponse: PartyDetailResponse = response.body()!!

                    Log.d("PARTY-DETAIL-RESPONSE", "PartyDataService-onResponse : code = " + partyDetailResponse.code)
                    Log.d("PARTY-DETAIL-RESPONSE", "PartyDataService-onResponse : message = " + partyDetailResponse.message)

                    when (partyDetailResponse.code) {
                        1000 -> partyDetailView.partyDetailSuccess(partyDetailResponse.result!!)
                        else -> partyDetailView.partyDetailFailure(partyDetailResponse.code, partyDetailResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<PartyDetailResponse>, t: Throwable) {
                Log.d("PARTY-DETAIL-RESPONSE", "PartyDataService-onFailure : PartyDetailSendFailed", t)
            }
        })
    }
}