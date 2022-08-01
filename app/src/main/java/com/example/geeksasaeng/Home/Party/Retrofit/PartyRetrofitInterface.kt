package com.example.geeksasaeng.Home.Party.Retrofit

import retrofit2.Call
import retrofit2.http.*

interface PartyRetrofitInterface {
    //@Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1ODgzMjA4NywiZXhwIjoxNjU5NzIxMTE5fQ.h2uzVXgnOs1dYZeQmrXHLaKjWVcnBvDy9haQj9QMO5A")
    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1OTI4MDQxMywiZXhwIjoxNjYwMTY5NDQ2fQ.QGsxRw4gkp-BUZczmTLzcJR-NHRDaSyEmeJaqW8JEZY")
    // 배달파티 상세조회
    @GET("delivery-party/{partyId}")
    fun getDeliveryPartyDetail(
        @Path("partyId") partyId: Int
    ): Call<PartyDetailResponse>

    //@Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1ODgzMjA4NywiZXhwIjoxNjU5NzIxMTE5fQ.h2uzVXgnOs1dYZeQmrXHLaKjWVcnBvDy9haQj9QMO5A")
    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1OTI4MDQxMywiZXhwIjoxNjYwMTY5NDQ2fQ.QGsxRw4gkp-BUZczmTLzcJR-NHRDaSyEmeJaqW8JEZY")
    @PATCH("delivery-party/{partyId}")
    fun sendDeleteDeliveryParty(
        @Path("partyId") partyId: Int
    ): Call<PartyDeleteResponse>

    //@Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1ODgzMjA4NywiZXhwIjoxNjU5NzIxMTE5fQ.h2uzVXgnOs1dYZeQmrXHLaKjWVcnBvDy9haQj9QMO5A")
    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1OTI4MDQxMywiZXhwIjoxNjYwMTY5NDQ2fQ.QGsxRw4gkp-BUZczmTLzcJR-NHRDaSyEmeJaqW8JEZY")
    @POST("reports/delivery-parties")
    fun reportDeliveryParty(
        @Body reportPartyRequest: PartyReportRequest
    ): Call<PartyReportResponse>

    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1OTI4MDQxMywiZXhwIjoxNjYwMTY5NDQ2fQ.QGsxRw4gkp-BUZczmTLzcJR-NHRDaSyEmeJaqW8JEZY")
    @POST("reports/members")
    fun reportDeliveryUser(
        @Body reportUserRequest: UserReportRequest
    ): Call<UserReportResponse>
}