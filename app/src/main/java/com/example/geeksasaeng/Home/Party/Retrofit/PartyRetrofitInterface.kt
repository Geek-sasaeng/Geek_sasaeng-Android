package com.example.geeksasaeng.Home.Party.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface PartyRetrofitInterface {
    // 배달파티 상세조회
    @GET("delivery-party/{partyId}")
    fun getDeliveryPartyDetail(
        @Header("Authorization") jwt: String?,
        @Path("partyId") partyId: Int
    ): Call<PartyDetailResponse>

    @PATCH("delivery-party/{partyId}")
    fun sendDeleteDeliveryParty(
        @Header("Authorization") jwt: String?,
        @Path("partyId") partyId: Int
    ): Call<PartyDeleteResponse>

    @POST("reports/delivery-parties")
    fun reportDeliveryParty(
        @Header("Authorization") jwt: String?,
        @Body reportPartyRequest: PartyReportRequest
    ): Call<PartyReportResponse>

    @POST("reports/members")
    fun reportDeliveryUser(
        @Header("Authorization") jwt: String?,
        @Body reportUserRequest: UserReportRequest
    ): Call<UserReportResponse>

    @POST("/deliveryPartyMember")
    fun joinDeliveryParty(
        @Header("Authorization") jwt: String?,
        @Body joinPartyRequest: JoinPartyRequest
    ): Call<JoinPartyResponse>
}