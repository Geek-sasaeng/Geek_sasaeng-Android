package com.example.geeksasaeng.Home.Party.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryResponse

interface PartyView {
}

interface PartyDetailView {
    fun partyDetailSuccess(result: PartyDetailResult)
    fun partyDetailFailure(code: Int, message: String)
}

interface PartyDeleteView {
    fun partyDeleteViewSuccess(code: Int)
    fun partyDeleteViewFailure(message: String)
}

interface PartyReportView {
    fun partyReportViewSuccess(code: Int)
    fun partyReportViewFailure(message: String)
    fun partyReportViewNetworkFailure()
}

interface UserReportView {
    fun userReportViewSuccess(code: Int)
    fun userReportViewFailure(message: String)
    fun userReportViewNetworkFailure()
}

interface JoinPartyView {
    fun joinPartyViewSuccess(code: Int)
    fun joinPartyViewFailure(message: String)
}