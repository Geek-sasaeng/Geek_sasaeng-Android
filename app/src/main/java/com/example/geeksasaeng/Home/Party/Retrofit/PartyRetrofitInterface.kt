package com.example.geeksasaeng.Home.Party.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface PartyRetrofitInterface {
    // 배달파티 상세조회
    @GET("delivery-party/{partyId}")
    fun getDeliveryPartyDetail(
        @Path("partyId") partyId: Int
    ): Call<PartyDetailResponse>

    //배달 파티 삭제
    @PATCH("delivery-party/{partyId}")
    fun sendDeleteDeliveryParty(
        @Path("partyId") partyId: Int
    ): Call<PartyDeleteResponse>

    @POST("reports/delivery-parties")
    fun reportDeliveryParty(
        @Body reportPartyRequest: PartyReportRequest
    ): Call<PartyReportResponse>

    @POST("reports/members")
    fun reportDeliveryUser(
        @Body reportUserRequest: UserReportRequest
    ): Call<UserReportResponse>

    @POST("/delivery-party-member")
    fun joinDeliveryParty(
        @Body joinPartyRequest: JoinPartyRequest
    ): Call<JoinPartyResponse>
}