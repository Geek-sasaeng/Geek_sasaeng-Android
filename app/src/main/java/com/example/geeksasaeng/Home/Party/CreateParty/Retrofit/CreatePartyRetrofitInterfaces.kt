package com.example.geeksasaeng.Home.CreateParty.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface CreatePartyRetrofitInterfaces {

    //기숙사 default-location
    /*@Headers("Authorization:Bearer \${getJwt()}")*/
    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjoyNn0sImlhdCI6MTY1Nzk0MTQ4NiwiZXhwIjoxNjU4ODMwNTE5fQ.n9HFrLuc97GeWOcKo-ffAj-k5XAvcd7IH0iEuOVzPaQ")
    @GET("/{dormitoryId}/default-location")
    fun getDeliveryPartyDefaultLocation(
        @Path("dormitoryId") dormitoryId:Int
    ): Call<CreatePartyDefaultLocResponse>

    //배달파티 생성하기
    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjoyNn0sImlhdCI6MTY1Nzk0MTQ4NiwiZXhwIjoxNjU4ODMwNTE5fQ.n9HFrLuc97GeWOcKo-ffAj-k5XAvcd7IH0iEuOVzPaQ")
    @POST("/{dormitoryId}/delivery-party")
    fun createParty(
        @Path("dormitoryId") dormitoryId:Int,
        @Body createPartyRequest: CreatePartyRequest
    ) : Call<CreatePartyResponse>
}