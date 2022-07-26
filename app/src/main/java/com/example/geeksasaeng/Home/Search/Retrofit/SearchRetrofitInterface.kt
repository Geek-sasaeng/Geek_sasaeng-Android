package com.example.geeksasaeng.Home.Search.Retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchRetrofitInterface {
    @Headers("Authorization: Bearer eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJqd3RJbmZvIjp7InVuaXZlcnNpdHlJZCI6MSwidXNlcklkIjoyNn0sImlhdCI6MTY1Nzk0MTQ4NiwiZXhwIjoxNjU4ODMwNTE5fQ.n9HFrLuc97GeWOcKo-ffAj-k5XAvcd7IH0iEuOVzPaQ")
    @GET("/{dormitoryId}/delivery-parties/keyword")
    fun getSearchPartyList(
        @Path("dormitoryId") dormitoryId: Int,
        @Query("cursor") cursor: Int,
        @Query("keyword") keyword: String
    ): Call<SearchResponse>
}