package com.example.geeksasaeng.Home.Delivery.Retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DeliveryPartyInterface {
    // 기숙사별 배달 리스트 불러오기
    @GET("/deliveryParties")
    fun getAllDeliveryList(
        @Query("/get") domitoryId: Int
    ): Call<DeliveryPartyListResponse>
}