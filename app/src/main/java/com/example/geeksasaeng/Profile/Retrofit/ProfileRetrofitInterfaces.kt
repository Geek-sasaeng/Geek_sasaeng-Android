package com.example.geeksasaeng.Profile.Retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

//공지사항 조회 api
interface ProfileRetrofitInterfaces {
    @POST("/announcement/detail")
    fun getAnnouncement(@Body profileAnnouncementRequest: ProfileAnnouncementRequest): Call<ProfileAnnouncementResponse>
}