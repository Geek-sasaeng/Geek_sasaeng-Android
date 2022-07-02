package com.example.geeksasaeng.Home.Delivery.Retrofit

data class DeliveryPartyListResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: List<DeliveryPartyResult>
)