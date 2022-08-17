package com.example.geeksasaeng.Profile.Retrofit

import android.util.Log
import com.example.geeksasaeng.Signup.Retrofit.SignUpResponse
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDataService {
    //뷰 객체 생성
    private lateinit var profileAnnouncementView: ProfileAnnouncementView

    private val ProfileDataService = retrofit.create(ProfileRetrofitInterfaces::class.java)

    //setVeiw
    fun setProfileAnnouncementView(profileAnnouncementView: ProfileAnnouncementView){
        this.profileAnnouncementView = profileAnnouncementView
    }

    //공지사항 조회
    fun ProfileAnnouncementSender( announcementId :ProfileAnnouncementRequest){
        ProfileDataService.getAnnouncement(announcementId).enqueue(object : Callback<ProfileAnnouncementResponse> {
            override fun onResponse(
                call: Call<ProfileAnnouncementResponse>,
                response: Response<ProfileAnnouncementResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp: ProfileAnnouncementResponse = response.body()!!
                    //TODO:SWAGGER업데이트 되면 반영
                    when(resp.code){
                        1000-> profileAnnouncementView.onProfileAnnouncementSuccess()
                        else-> profileAnnouncementView.onProfileAnnouncementFailure(resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ProfileAnnouncementResponse>, t: Throwable) {
                Log.d("ANNOUNCE-RESPONSE", "ProfileDataService-onFailure : getAnnounceFailed", t)
            }
        })
    }
}