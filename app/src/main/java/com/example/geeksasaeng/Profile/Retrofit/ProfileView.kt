package com.example.geeksasaeng.Profile.Retrofit

// 최근 활동 3개 조회
interface ProfileRecentActivityView {
    fun onProfileRecentActivitySuccess(result: ArrayList<ProfileRecentActivityResult>?)
    fun onProfileAnnouncementFailure(message: String)
}

// 공지사항 전체 조회
interface ProfileAnnouncementView {
    fun onProfileAnnouncementSuccess(result: ArrayList<ProfileAnnouncementResult>)
    fun onProfileAnnouncementFailure(message: String)
}

// 공지사항 상세 조회
interface ProfileAnnouncementDetailView{
    fun onProfileAnnouncementDetailSuccess(result: ProfileAnnouncementDeatilResult)
    fun onProfileAnnouncementDetailFailure(message: String)
}

// 내 정보 조회
interface ProfileMyAccountView {
    fun onProfileMyAccountSuccess(result: ProfileMyAccountResult)
    fun onProfileMyAccountFailure(message: String)
}