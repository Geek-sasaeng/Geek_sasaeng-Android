package com.example.geeksasaeng.Home.CreateParty.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface CreatePartyRetrofitInterfaces {

    //기숙사 default-location
    @GET("/{dormitoryId}/default-location")
    fun getDeliveryPartyDefaultLocation(
        @Header("Authorization") jwt: String?,
        @Path("dormitoryId") dormitoryId:Int
    ): Call<CreatePartyDefaultLocResponse>

    //배달파티 생성하기
    @POST("/{dormitoryId}/delivery-party")
    fun createParty(
        @Header("Authorization") jwt: String?,
        @Path("dormitoryId") dormitoryId:Int,
        @Body createPartyRequest: CreatePartyRequest
    ) : Call<CreatePartyResponse>
}