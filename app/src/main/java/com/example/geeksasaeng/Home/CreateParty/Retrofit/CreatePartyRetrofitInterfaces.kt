package com.example.geeksasaeng.Home.CreateParty.Retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CreatePartyRetrofitInterfaces {

    //기숙사 default-location
    @GET("/{dormitoryId}/default-location")
    fun getDeliveryPartyDefaultLocation(
        @Path("dormitoryId") dormitoryId:CreatePartyDefaultLocRequest
    ): Call<CreatePartyDefaultLocResponse>
}