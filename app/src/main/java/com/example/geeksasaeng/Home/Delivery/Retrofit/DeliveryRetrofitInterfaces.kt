package com.example.geeksasaeng.Home.Delivery.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryBannerResponse
import com.example.geeksasaeng.Home.Delivery.DeliveryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeliveryRetrofitInterfaces {

    //전체 광고 조회
    @GET("/commercials")
    fun getCommercials(

    ) : Call<DeliveryBannerResponse>


    // 기숙사별 배달 리스트 불러오기
    @GET("/{dormitoryId}/delivery-parties")
    fun getAllDeliveryList(
        @Path("dormitoryId") dormitoryId: Int,
        @Query("cursor") cursor: Int)
    : Call<DeliveryResponse>
}