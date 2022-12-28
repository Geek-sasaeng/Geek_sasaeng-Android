package com.example.geeksasaeng.Profile.Retrofit

import retrofit2.Call
import retrofit2.http.*


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

    // 진행 중인 활동 조회 api
    @GET("/delivery-parties/recent/ongoing")
    fun getRecentActivity(
        @Header("Authorization") jwt: String?
    ): Call<ProfileMyOngoingActivityResponse>

    // 나의 정보 조회 api
    @GET("/members")
    fun getMyInfo(
        @Header("Authorization") jwt: String?
    ): Call<ProfileMyInfoResponse>

    // 진행했던 활동 조회 api
    @GET("/delivery-parties/end")
    fun getMyPreActivity(
        @Header("Authorization") jwt: String?,
        @Query("cursor") cursor: Int
    ): Call<ProfileMyPreActivityResponse>

    // 회원 탈퇴 api
    @PATCH("/members/account-delete/{id}")
    fun profileWithdrawal(
        @Header("Authorization") jwt: String?,
        @Path("id") id: Int,
        @Body profileWithdrawalRequest: ProfileWithdrawalRequest
    ): Call<ProfileWithdrawalResponse>
}