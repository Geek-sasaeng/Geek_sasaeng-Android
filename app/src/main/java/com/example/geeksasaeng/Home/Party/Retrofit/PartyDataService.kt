package com.example.geeksasaeng.Home.Party.Retrofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.geeksasaeng.Home.Delivery.Retrofit.DeliveryRetrofitInterfaces
import com.example.geeksasaeng.Home.Party.ReportParty.DialogReportParty
import com.example.geeksasaeng.Utils.ApplicationClass
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PartyDataService {

    //뷰 객체 생성
    private lateinit var partyDetailView: PartyDetailView
    private lateinit var partyDeleteView: PartyDeleteView
    private lateinit var partyReportView: PartyReportView
    private lateinit var userReportView: UserReportView
    private lateinit var joinPartyView: JoinPartyView

    val PartyService = NetworkModule.getInstance()?.create(PartyRetrofitInterface::class.java)

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
    fun setUserReportView(userReportView: UserReportView) {
        this.userReportView = userReportView
    }
    fun setJoinPartyView(joinPartyView: JoinPartyView) {
        this.joinPartyView = joinPartyView
    }

    //파티보기 상세조회
    fun partyDetailSender(partyId: Int) {
        PartyService?.getDeliveryPartyDetail(partyId)?.enqueue(object : Callback<PartyDetailResponse> {
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
        PartyService?.sendDeleteDeliveryParty(partyId)?.enqueue(object : Callback<PartyDeleteResponse> {
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
        PartyService?.reportDeliveryParty(partyReportRequest)?.enqueue(object : Callback<PartyReportResponse> {
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
                partyReportView.partyReportViewNetworkFailure()
            }
        })
    }

    // 사용자 신고하기
    fun userReportSender(userReportRequest: UserReportRequest) {
        PartyService?.reportDeliveryUser(userReportRequest)?.enqueue(object : Callback<UserReportResponse> {
            override fun onResponse(call: Call<UserReportResponse>, response: Response<UserReportResponse>) {
                Log.d("USER-REPORT-RESPONSE", "PartyDataService-onResponse : response.code = " + response.code())
                Log.d("USER-REPORT-RESPONSE", "PartyDataService-onResponse : response.isSuccessful = " + response.isSuccessful)

                if (response.isSuccessful && response.code() == 200) {
                    val userReportResponse: UserReportResponse = response.body()!!

                    Log.d("USER-REPORT-RESPONSE", "PartyDataService-onResponse : code = " + userReportResponse.code)
                    Log.d("USER-REPORT-RESPONSE", "PartyDataService-onResponse : message = " + userReportResponse.message)

                    when (userReportResponse.code) {
                        1000 -> userReportView.userReportViewSuccess(userReportResponse.code)
                        else -> userReportView.userReportViewFailure(userReportResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<UserReportResponse>, t: Throwable) {
                Log.d("USER-REPORT-RESPONSE", "PartyDataService-onFailure : PartyDetailSendFailed", t)
                userReportView.userReportViewNetworkFailure()
            }
        })
    }

    // 배달 파티 참여하기
    fun joinPartySender(joinPartyRequest: JoinPartyRequest) {
        PartyService?.joinDeliveryParty(joinPartyRequest)?.enqueue(object: Callback<JoinPartyResponse> {
            override fun onResponse(call: Call<JoinPartyResponse>, response: Response<JoinPartyResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val joinPartyResponse: JoinPartyResponse = response.body()!!

                    when (joinPartyResponse.code) {
                        1000 -> joinPartyView.joinPartyViewSuccess(joinPartyResponse.code)
                        4000 -> Log.d("JOIN-PARTY", "서버 오류")
                        else -> joinPartyView.joinPartyViewFailure(joinPartyResponse.message)
                    }
                }
            }
            override fun onFailure(call: Call<JoinPartyResponse>, t: Throwable) {
                Log.d("Party-Response", "PartyDataService-joinPartySender-onFailure", t)
            }
        })
    }
}