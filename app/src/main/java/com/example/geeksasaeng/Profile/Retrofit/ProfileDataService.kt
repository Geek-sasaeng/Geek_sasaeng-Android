package com.example.geeksasaeng.Profile.Retrofit

import android.util.Log
import com.example.geeksasaeng.Signup.Retrofit.SignUpResponse
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDataService {
    //뷰 객체 생성
    private lateinit var profileAnnouncementView: ProfileAnnouncementView
    private lateinit var profileAnnouncementDetailView: ProfileAnnouncementDetailView

    private val ProfileDataService = retrofit.create(ProfileRetrofitInterfaces::class.java)

    //setVeiw
    fun setProfileAnnouncementView(profileAnnouncementView: ProfileAnnouncementView){
        this.profileAnnouncementView = profileAnnouncementView
    }

    fun setProfileAnnouncementDetailView(profileAnnouncementDetailView: ProfileAnnouncementDetailView){
        this.profileAnnouncementDetailView = profileAnnouncementDetailView
    }


    //공지사항 전체 조회
    fun ProfileAnnouncementSender( ){
        Log.d("announcement", "Bearer " + getJwt())
        ProfileDataService.getAnnouncementList("Bearer " + getJwt()).enqueue(object : Callback<ProfileAnnouncementResponse> {
            override fun onResponse(
                call: Call<ProfileAnnouncementResponse>,
                response: Response<ProfileAnnouncementResponse>
            ) {
                Log.d("announcement", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val resp: ProfileAnnouncementResponse = response.body()!!
                    Log.d("announcement-resp", resp.toString())
                    when(resp.code){
                        1000-> profileAnnouncementView.onProfileAnnouncementSuccess(resp.result)
                        else-> profileAnnouncementView.onProfileAnnouncementFailure(resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ProfileAnnouncementResponse>, t: Throwable) {
                Log.d("ANNOUNCE-RESPONSE", "ProfileDataService-onFailure : getAnnounceFailed", t)
            }
        })
    }

    //공지사항 상세 조회
    fun ProfileAnnouncementDetailSender( announcementId :ProfileAnnouncementDetailRequest){
        ProfileDataService.getAnnouncementDetail("Bearer " + getJwt(), announcementId).enqueue(object : Callback<ProfileAnnouncementDetailResponse> {
            override fun onResponse(
                call: Call<ProfileAnnouncementDetailResponse>,
                response: Response<ProfileAnnouncementDetailResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp: ProfileAnnouncementDetailResponse = response.body()!!
                    //TODO:SWAGGER업데이트 되면 반영
                    when(resp.code){
                        1000-> profileAnnouncementDetailView.onProfileAnnouncementDetailSuccess(resp.result!!)
                        else-> profileAnnouncementDetailView.onProfileAnnouncementDetailFailure(resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ProfileAnnouncementDetailResponse>, t: Throwable) {
                Log.d("ANNOUNCE-RESPONSE", "ProfileDataService-onFailure : getAnnounceFailed", t)
            }
        })
    }
}