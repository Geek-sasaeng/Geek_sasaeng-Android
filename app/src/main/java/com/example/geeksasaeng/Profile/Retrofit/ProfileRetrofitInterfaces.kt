package com.example.geeksasaeng.Profile.Retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface ProfileRetrofitInterfaces {
    //공지사항 전체조회 api
    @GET("/announcements")
    fun getAnnouncementList(@Header("Authorization") jwt: String?,): Call<ProfileAnnouncementResponse>

    //공지사항 상세조회 api
    @POST("/announcement/detail")
    fun getAnnouncementDetail(
        @Header("Authorization") jwt: String?,
        @Body profileAnnouncementDetailRequest: ProfileAnnouncementDetailRequest
    ): Call<ProfileAnnouncementDetailResponse>
}
