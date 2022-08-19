package com.example.geeksasaeng.Profile.Retrofit

interface ProfileRecentActivityView {
    fun onProfileRecentActivitySuccess(result: ProfileRecentActivityResult)
    fun onProfileAnnouncementFailure(message: String)
}

interface ProfileAnnouncementView {
    fun onProfileAnnouncementSuccess()
    fun onProfileAnnouncementFailure(message: String)
}