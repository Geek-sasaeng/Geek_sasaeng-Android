package com.example.geeksasaeng.Profile.Retrofit

import android.util.Log
import com.example.geeksasaeng.Signup.Retrofit.SignUpResponse
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDataService {

    //뷰 객체 생성
    private lateinit var profileRecentActivityView: ProfileRecentActivityView
    private lateinit var profileAnnouncementView: ProfileAnnouncementView
    private lateinit var profileMyAccountView: ProfileMyAccountView

    private val ProfileDataService = retrofit.create(ProfileAnnouncementRetrofitInterfaces::class.java)

    // setView
    fun setProfileRecentActivityView(profileRecentActivityView: ProfileRecentActivityView) {
        this.profileRecentActivityView = profileRecentActivityView
    }
    fun setProfileAnnouncementView(profileAnnouncementView: ProfileAnnouncementView){
        this.profileAnnouncementView = profileAnnouncementView
    }
    fun setMyAccountView(profileMyAccountView: ProfileMyAccountView) {
        this.profileMyAccountView = profileMyAccountView
    }

    // 최근 활동 3개 조회
    fun profileRecentActivitySender() {
        val profileRecentActivityService = NetworkModule.getInstance()?.create(ProfileRecentActivityRetrofitInterfaces::class.java)
        profileRecentActivityService?.getRecentActivity("Bearer " + getJwt())?.enqueue(object : Callback<ProfileRecentActivityResponse> {
            override fun onResponse(call: Call<ProfileRecentActivityResponse>, response: Response<ProfileRecentActivityResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp: ProfileRecentActivityResponse = response.body()!!
                    when (resp.code) {
                        1000 -> profileRecentActivityView.onProfileRecentActivitySuccess(resp.result!!)
                        4000 -> Log.d("PROFILE_RECENT_ACTIVITY", "서버 오류")
                        else -> profileRecentActivityView.onProfileAnnouncementFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<ProfileRecentActivityResponse>, t: Throwable) {
                Log.d("PROFILE-RECENT-ACTIVITY", "ProfileDataService-onFailure : getRecentActivity", t)
            }
        })
    }

    // 공지사항 조회
    fun profileAnnouncementSender(announcementId :ProfileAnnouncementRequest){
        val profileDataService = NetworkModule.getInstance()?.create(ProfileAnnouncementRetrofitInterfaces::class.java)
        profileDataService?.getAnnouncement(announcementId)?.enqueue(object : Callback<ProfileAnnouncementResponse> {
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

    // 나의 정보 조회
    fun profileMyAccountSender() {
        val profileMyAccountService = NetworkModule.getInstance()?.create(ProfileMyAccountRetrofitInterfaces::class.java)
        profileMyAccountService?.getMyAccount("Bearer " + getJwt())?.enqueue(object: Callback<ProfileMyAccountResponse> {
            override fun onResponse(call: Call<ProfileMyAccountResponse>, response: Response<ProfileMyAccountResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> profileMyAccountView.onProfileMyAccountSuccess(resp.result)
                        4000 -> Log.d("PROFILE-DATA-SERVICE", "서버 오류")
                        else -> profileMyAccountView.onProfileMyAccountFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<ProfileMyAccountResponse>, t: Throwable) {
                Log.d("MY-ACCOUNT-RESPONSE", "ProfileDataService-onFailure : getMyAccountFailed", t)
            }

        })
    }
}