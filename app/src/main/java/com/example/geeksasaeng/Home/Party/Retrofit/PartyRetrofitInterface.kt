package com.example.geeksasaeng.Home.Party.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface PartyRetrofitInterface {
    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjoyNn0sImlhdCI6MTY1Nzk0MTQ4NiwiZXhwIjoxNjU4ODMwNTE5fQ.n9HFrLuc97GeWOcKo-ffAj-k5XAvcd7IH0iEuOVzPaQ")
    // 배달파티 상세조회
    @GET("delivery-party/{partyId}")
    fun getDeliveryPartyDetail(
        @Path("partyId") partyId: Int
    ): Call<PartyDetailResponse>
}