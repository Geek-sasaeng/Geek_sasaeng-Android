package com.example.geeksasaeng.Profile.Retrofit

interface ProfileRecentActivityView {
    fun onProfileRecentActivitySuccess(result: ArrayList<ProfileRecentActivityResult>?)
    fun onProfileAnnouncementFailure(message: String)
}

interface ProfileAnnouncementView {
    fun onProfileAnnouncementSuccess()
    fun onProfileAnnouncementFailure(message: String)
}