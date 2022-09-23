package com.example.geeksasaeng.Profile.Retrofit

import retrofit2.Call
import retrofit2.http.*


interface ProfileRetrofitInterfaces {
    //공지사항 전체조회 api
    @GET("/announcements")
    fun getAnnouncementList(
    ): Call<ProfileAnnouncementResponse>

    //공지사항 상세조회 api
    @POST("/announcement/detail")
    fun getAnnouncementDetail(
        @Body profileAnnouncementDetailRequest: ProfileAnnouncementDetailRequest
    ): Call<ProfileAnnouncementDetailResponse>
}

// 진행 중인 활동 조회 api
interface ProfileMyOngoingActivityRetrofitInterfaces {
    @GET("/delivery-parties/recent/ongoing")
    fun getRecentActivity(
    ): Call<ProfileMyOngoingActivityResponse>
}

// 나의 정보 조회 api
interface ProfileMyInfoRetrofitInterfaces {
    @GET("/members")
    fun getMyInfo(
    ): Call<ProfileMyInfoResponse>
}

// 진행했던 활동 조회 api
interface ProfileMyPreActivityRetrofitInterfaces {
    @GET("/delivery-parties/end")
    fun getMyPreActivity(
        @Query("cursor") cursor: Int
    ): Call<ProfileMyPreActivityResponse>
}

// 회원 탈퇴 api
interface ProfileWithdrawalRetrofitInterfaces {
    @PATCH("/members/account-delete/{id}")
    fun profileWithdrawal(
        @Path("id") id: Int,
        @Body profileWithdrawalRequest: ProfileWithdrawalRequest
    ): Call<ProfileWithdrawalResponse>
}