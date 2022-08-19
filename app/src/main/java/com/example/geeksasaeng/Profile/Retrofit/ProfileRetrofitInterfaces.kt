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

// 최근 활동 3개 api
interface ProfileRecentActivityRetrofitInterfaces {
    @GET("/delivery-parties/recent")
    fun getRecentActivity(
        @Header("Authorization") jwt: String?
    ): Call<ProfileRecentActivityResponse>
}

// 나의 정보 조회 api
interface ProfileMyAccountRetrofitInterfaces {
    @GET("/members")
    fun getMyAccount(
        @Header("Authorization") jwt: String?
    ): Call<ProfileMyAccountResponse>
}