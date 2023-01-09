package com.example.geeksasaeng.Home.Search.Retrofit

import com.example.geeksasaeng.Home.Delivery.DeliveryResponse
import retrofit2.Call
import retrofit2.http.*

interface SearchRetrofitInterface {
    @GET("/{dormitoryId}/delivery-parties/keyword")
    fun getSearchPartyList(
        @Path("dormitoryId") dormitoryId: Int,
        @Query("cursor") cursor: Int,
        @Query("keyword") keyword: String
    ): Call<SearchResponse>

    // 기숙사별 배달 리스트 필터 적용 후 불러오기
    @GET("/{dormitoryId}/delivery-parties/keyword")
    fun getFilterSearchList(
        @Path("dormitoryId") dormitoryId: Int,
        @Query("cursor") cursor: Int,
        @Query("keyword") keyword: String,
        @Query("orderTimeCategory") orderTimeCategory: String?,
        @Query("maxMatching") maxMatching: Int?
    ): Call<SearchResponse>
}