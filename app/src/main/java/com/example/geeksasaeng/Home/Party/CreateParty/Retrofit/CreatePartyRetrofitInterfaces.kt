package com.example.geeksasaeng.Home.CreateParty.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface CreatePartyRetrofitInterfaces {

    //기숙사 default-location
    /*@Headers("Authorization:Bearer \${getJwt()}")*/
    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1ODgzMjA4NywiZXhwIjoxNjU5NzIxMTE5fQ.h2uzVXgnOs1dYZeQmrXHLaKjWVcnBvDy9haQj9QMO5A")
    @GET("/{dormitoryId}/default-location")
    fun getDeliveryPartyDefaultLocation(
        @Path("dormitoryId") dormitoryId:Int
    ): Call<CreatePartyDefaultLocResponse>

    //배달파티 생성하기
    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1ODgzMjA4NywiZXhwIjoxNjU5NzIxMTE5fQ.h2uzVXgnOs1dYZeQmrXHLaKjWVcnBvDy9haQj9QMO5A")
    @POST("/delivery-party")
    fun createParty(@Body createPartyRequest: CreatePartyRequest) : Call<CreatePartyResponse>
}