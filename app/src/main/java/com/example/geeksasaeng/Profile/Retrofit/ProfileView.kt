package com.example.geeksasaeng.Profile.Retrofit

//공지사항 전체 조회
interface ProfileAnnouncementView {
    fun onProfileAnnouncementSuccess(result: ArrayList<ProfileAnnouncementResult>)
    fun onProfileAnnouncementFailure(message: String)
}

//공지사항 상세 조회
interface ProfileAnnouncementDetailView{
    fun onProfileAnnouncementDetailSuccess(result: ProfileAnnouncementDeatilResult)
    fun onProfileAnnouncementDetailFailure(message: String)
}