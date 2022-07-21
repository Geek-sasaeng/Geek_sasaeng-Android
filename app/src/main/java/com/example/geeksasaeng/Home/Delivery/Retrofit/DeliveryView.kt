package com.example.geeksasaeng.Home.Delivery.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryBannerResult
import com.example.geeksasaeng.Home.Delivery.DeliveryResponse

interface DeliveryBannerView{
    fun ondeliveryBannerSuccess(results: Array<DeliveryBannerResult>)
    fun ondeliveryBannerFailure(message: String)
}

interface DeliveryView {
    fun deliverySuccess(response: DeliveryResponse)
    fun deliveryFailure(code: Int, message: String)
}