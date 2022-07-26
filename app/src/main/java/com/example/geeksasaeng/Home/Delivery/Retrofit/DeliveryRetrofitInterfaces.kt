package com.example.geeksasaeng.Home.Delivery.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryBannerResponse
import com.example.geeksasaeng.Home.Delivery.DeliveryResponse
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface DeliveryRetrofitInterfaces {
    //전체 광고 조회
    @GET("/commercials")
    fun getCommercials(): Call<DeliveryBannerResponse>

    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1ODgzMjA4NywiZXhwIjoxNjU5NzIxMTE5fQ.h2uzVXgnOs1dYZeQmrXHLaKjWVcnBvDy9haQj9QMO5A")
    // @Headers("Authorization:Bearer \${getJwt()}")
    // 기숙사별 배달 리스트 불러오기
    @GET("/{dormitoryId}/delivery-parties")
    fun getAllDeliveryList(
        @Path("dormitoryId") dormitoryId: Int,
        @Query("cursor") cursor: Int
    ): Call<DeliveryResponse>
}