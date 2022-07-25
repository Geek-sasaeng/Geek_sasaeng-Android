package com.example.geeksasaeng.Home.Party.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryResponse

interface PartyView {
}

interface PartyDetailView {
    fun partyDetailSuccess(result: PartyDetailResult)
    fun partyDetailFailure(code: Int, message: String)
}