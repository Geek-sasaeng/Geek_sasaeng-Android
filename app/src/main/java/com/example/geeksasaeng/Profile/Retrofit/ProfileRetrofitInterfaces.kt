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

    // 진행 중인 활동 조회 api
    @GET("/delivery-parties/recent/ongoing")
    fun getRecentActivity(
    ): Call<ProfileMyOngoingActivityResponse>

    // 나의 정보 조회 api
    @GET("/members")
    fun getMyInfo(
    ): Call<ProfileMyInfoResponse>

    // 진행했던 활동 조회 api
    @GET("/delivery-parties/end")
    fun getMyPreActivity(
        @Query("cursor") cursor: Int
    ): Call<ProfileMyPreActivityResponse>

    // 회원 탈퇴 api
    @PATCH("/members/account-delete/{id}")
    fun profileWithdrawal(
        @Path("id") id: Int,
        @Body profileWithdrawalRequest: ProfileWithdrawalRequest
    ): Call<ProfileWithdrawalResponse>

    // 멤버 정보 수정하기
    @POST("/members/info")
    fun profileMemberInfoModify(
        @Path("checkPassword") checkPassword: String?,
        @Path("dormitoryId") dormitoryId: Int,
        @Path("loginId") loginId: String,
        @Path("nickname") nickname: String,
        @Path("password") password: String?,
        @Path("profileImg") profileImg: String?,
    ): Call<ProfileMemberInfoModifyResponse>

    //비밀번호 일치 확인
    @POST("/members/password")
    fun profilePasswordChecking(
        @Body profilePasswordCheckingRequest: ProfilePasswordCheckingRequest
    ) : Call<ProfilePasswordCheckingResponse>

    //비밀번호 수정
    @PATCH("/members/password")
    fun profilePasswordChange(
        @Body profilePasswordChangeRequest: ProfilePasswordChangeRequest
    ) : Call<ProfilePasswordChangeResponse>


}