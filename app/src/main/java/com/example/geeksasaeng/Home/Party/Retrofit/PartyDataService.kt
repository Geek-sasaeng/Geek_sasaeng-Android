package com.example.geeksasaeng.Home.Party.Retrofit

import android.util.Log
import com.example.geeksasaeng.Home.CreateParty.Retrofit.CreatePartyRequest
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
    private lateinit var partyDeleteView: PartyDeleteView
    private lateinit var partyReportView: PartyReportView

    //setView
    fun setPartyDetailView(partyDetailView: PartyDetailView) {
        this.partyDetailView = partyDetailView
    }
    fun setPartyDeleteView(partyDeleteView: PartyDeleteView) {
        this.partyDeleteView = partyDeleteView
    }
    fun setPartyReportView(partyReportView: PartyReportView) {
        this.partyReportView = partyReportView
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

    // 파티 삭제하기
    fun partyDeleteSender(partyId: Int) {
        val partyDeleteService = NetworkModule.getInstance()?.create(PartyRetrofitInterface::class.java)
        partyDeleteService?.sendDeleteDeliveryParty(partyId)?.enqueue(object : Callback<PartyDeleteResponse> {
            override fun onResponse(call: Call<PartyDeleteResponse>, response: Response<PartyDeleteResponse>) {
                Log.d("PARTY-DELETE-RESPONSE", "PartyDataService-onResponse : response.code = " + response.code())
                Log.d("PARTY-DELETE-RESPONSE", "PartyDataService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val partyDetailResponse: PartyDeleteResponse = response.body()!!

                    Log.d("PARTY-DELETE-RESPONSE", "PartyDataService-onResponse : code = " + partyDetailResponse.code)
                    Log.d("PARTY-DELETE-RESPONSE", "PartyDataService-onResponse : message = " + partyDetailResponse.message)

                    when (partyDetailResponse.code) {
                        1000 -> partyDeleteView.partyDeleteViewSuccess(partyDetailResponse.code)
                        else -> partyDeleteView.partyDeleteViewFailure(partyDetailResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<PartyDeleteResponse>, t: Throwable) {
                Log.d("PARTY-DELETE-RESPONSE", "PartyDataService-onFailure : PartyDetailSendFailed", t)
            }
        })
    }

    // 파티 신고하기
    fun partyReportSender(partyReportRequest: PartyReportRequest) {
        val partyReportService = NetworkModule.getInstance()?.create(PartyRetrofitInterface::class.java)
        partyReportService?.reportDeliveryParty(partyReportRequest)?.enqueue(object : Callback<PartyReportResponse> {
            override fun onResponse(call: Call<PartyReportResponse>, response: Response<PartyReportResponse>) {
                Log.d("PARTY-REPORT-RESPONSE", "PartyDataService-onResponse : response.code = " + response.code())
                Log.d("PARTY-REPORT-RESPONSE", "PartyDataService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val partyReportResponse: PartyReportResponse = response.body()!!

                    Log.d("PARTY-REPORT-RESPONSE", "PartyDataService-onResponse : code = " + partyReportResponse.code)
                    Log.d("PARTY-REPORT-RESPONSE", "PartyDataService-onResponse : message = " + partyReportResponse.message)

                    when (partyReportResponse.code) {
                        1000 -> partyReportView.partyReportViewSuccess(partyReportResponse.code)
                        else -> partyReportView.partyReportViewFailure(partyReportResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<PartyReportResponse>, t: Throwable) {
                Log.d("PARTY-REPORT-RESPONSE", "PartyDataService-onFailure : PartyDetailSendFailed", t)
            }
        })
    }
}