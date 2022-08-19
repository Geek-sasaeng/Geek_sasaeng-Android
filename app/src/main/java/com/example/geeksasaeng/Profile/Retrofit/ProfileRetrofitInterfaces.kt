package com.example.geeksasaeng.Profile.Retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// 최근 활동 3개 api
interface ProfileRecentActivityRetrofitInterfaces {
    @GET("/delivery-parties/recent")
    fun getRecentActivity(): Call<ProfileRecentActivityResponse>
}

//공지사항 조회 api
interface ProfileAnnouncementRetrofitInterfaces {
    @POST("/announcement/detail")
    fun getAnnouncement(@Body profileAnnouncementRequest: ProfileAnnouncementRequest): Call<ProfileAnnouncementResponse>
}