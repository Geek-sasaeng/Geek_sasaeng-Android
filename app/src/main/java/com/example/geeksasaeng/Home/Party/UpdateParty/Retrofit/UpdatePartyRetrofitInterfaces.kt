package com.example.geeksasaeng.Home.Party.UpdateParty.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface UpdatePartyRetrofitInterfaces {

    //배달파티 수정하기
    //eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjoyNn0sImlhdCI6MTY1Nzk0MTQ4NiwiZXhwIjoxNjU4ODMwNTE5fQ.n9HFrLuc97GeWOcKo-ffAj-k5XAvcd7IH0iEuOVzPaQ
    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjoyNn0sImlhdCI6MTY1ODgzMzQwMSwiZXhwIjoxNjU5NzIyNDMzfQ.dVkuV8h-NWUPZ8qLBOM2DBwYCTQrQU3skcF3uLgD4Yc")
    @PUT("/{dormitoryId}/delivery-party/{partyId}")
    fun updateParty(
        @Path("dormitoryId") dormitoryId:Int,
        @Body updatePartyRequest: UpdatePartyRequest,
        @Path("partyId") partyId:Int
    ) : Call<UpdatePartyResponse>
}