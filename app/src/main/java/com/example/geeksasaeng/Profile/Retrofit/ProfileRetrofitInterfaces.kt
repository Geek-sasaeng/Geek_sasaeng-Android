package com.example.geeksasaeng.Profile.Retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    @PATCH("/members/account-delete")
    fun profileWithdrawal(): Call<ProfileWithdrawalResponse>

    // 멤버 정보 수정하기
    @Multipart
    @POST("/members/info")
    fun profileMemberInfoModify(
        @Part profileImg: MultipartBody.Part?,
        @PartMap data: HashMap<String, RequestBody>
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

    //대학교별 기숙사 목록 조회
    @GET("/{universityId}/dormitories")
    fun profileViewDormitory(
        @Path("universityId") universityId: Int
    ): Call<ProfileViewDormitoryResponse>

    //로그아웃-(Fcm토큰 최신화때문에 로그아웃시 해당 멤버 fcm 정보 삭제)
    @DELETE("/logout")
    fun profileLogout(
    ): Call<ProfileLogoutResponse>


}