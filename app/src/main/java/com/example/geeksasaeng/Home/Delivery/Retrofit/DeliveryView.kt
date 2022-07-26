package com.example.geeksasaeng.Home.Delivery.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryBannerResult
import com.example.geeksasaeng.Home.Delivery.DeliveryResponse
import com.example.geeksasaeng.Home.Delivery.DeliveryResult

interface DeliveryBannerView{
    fun ondeliveryBannerSuccess(results: Array<DeliveryBannerResult>)
    fun ondeliveryBannerFailure(message: String)
}

interface DeliveryView {
    fun deliverySuccess(result: DeliveryResult)
    fun deliveryFailure(code: Int, message: String)
}