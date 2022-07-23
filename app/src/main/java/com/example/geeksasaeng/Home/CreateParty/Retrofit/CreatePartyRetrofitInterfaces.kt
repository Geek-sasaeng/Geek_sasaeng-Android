package com.example.geeksasaeng.Home.CreateParty.Retrofit

import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.http.*

interface CreatePartyRetrofitInterfaces {

    //기숙사 default-location
    @Headers("Authorization:Bearer \${getJwt()}")
    @GET("/{dormitoryId}/default-location")
    fun getDeliveryPartyDefaultLocation(
        @Path("dormitoryId") dormitoryId:CreatePartyDefaultLocRequest
    ): Call<CreatePartyDefaultLocResponse>

    //배달파티 생성하기
    @POST("/delivery-party")
    fun createParty(@Body createPartyRequest: CreatePartyRequest) : Call<CreatePartyResponse>
}