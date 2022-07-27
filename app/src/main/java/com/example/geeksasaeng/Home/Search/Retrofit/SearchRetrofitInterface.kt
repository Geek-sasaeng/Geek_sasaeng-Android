package com.example.geeksasaeng.Home.Search.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchRetrofitInterface {
    @Headers("Authorization: Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1ODgzMjA4NywiZXhwIjoxNjU5NzIxMTE5fQ.h2uzVXgnOs1dYZeQmrXHLaKjWVcnBvDy9haQj9QMO5A")
    @GET("/{dormitoryId}/delivery-parties/keyword")
    fun getSearchPartyList(
        @Path("dormitoryId") dormitoryId: Int,
        @Query("cursor") cursor: Int,
        @Query("keyword") keyword: String
    ): Call<SearchResponse>

    @Headers("Authorization:Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjozNH0sImlhdCI6MTY1ODgzMjA4NywiZXhwIjoxNjU5NzIxMTE5fQ.h2uzVXgnOs1dYZeQmrXHLaKjWVcnBvDy9haQj9QMO5A")
    // @Headers("Authorization:Bearer \${getJwt()}")
    // 기숙사별 배달 리스트 필터 적용 후 불러오기
    @GET("/{dormitoryId}/delivery-parties")
    fun getFilterSearchList(
        @Path("dormitoryId") dormitoryId: Int,
        @Query("cursor") cursor: Int,
        @Query("orderTimeCategory") orderTimeCategory: String,
        @Query("maxMatching") maxMatching: Int
    ): Call<SearchResponse>
}