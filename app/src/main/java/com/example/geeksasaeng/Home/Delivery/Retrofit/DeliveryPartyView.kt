package com.example.geeksasaeng.Home.Delivery.Retrofit

interface DeliveryPartyView {
    fun deliveryPartyListSuccess(listResponse:DeliveryPartyListResponse)
    fun deliveryPartyListFailure(message:String)
}