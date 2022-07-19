package com.example.geeksasaeng.Home.Delivery.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryResponse

interface DeliveryView {
    fun deliverySuccess(response: DeliveryResponse)
    fun deliveryFailure(code: Int, message: String)
}