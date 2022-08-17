package com.example.geeksasaeng.Chatting.Retrofit

import com.example.geeksasaeng.Home.Party.Retrofit.PartyDeleteResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ChattingRetrofitInterfaces {

    //배달파티 수동 매칭마감
    @PATCH("/delivery-party/{roomUuid}/matching-status")
    fun matchingEnd(
        @Header("Authorization") jwt: String?,
        @Path("roomUuid") roomUuid: String
    ): Call<MatchingEndResponse>
}