package com.example.geeksasaeng.Home.Party.UpdateParty.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface UpdatePartyRetrofitInterfaces {
    //배달파티 수정하기
    @PUT("/{dormitoryId}/delivery-party/{partyId}")
    fun updateParty(
        @Path("dormitoryId") dormitoryId:Int,
        @Body updatePartyRequest: UpdatePartyRequest,
        @Path("partyId") partyId:Int
    ) : Call<UpdatePartyResponse>
}