package com.example.geeksasaeng.Profile.Retrofit

import android.util.Log
import com.example.geeksasaeng.Signup.Retrofit.SignUpResponse
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDataService {

    //뷰 객체 생성
    private lateinit var profileRecentActivityView: ProfileRecentActivityView
    private lateinit var profileAnnouncementView: ProfileAnnouncementView
    private lateinit var profileAnnouncementDetailView: ProfileAnnouncementDetailView
    private lateinit var profileMyInfoView: ProfileMyInfoView
    private lateinit var profileWithdrawalView: ProfileWithdrawalView

    // setView
    fun setProfileRecentActivityView(profileRecentActivityView: ProfileRecentActivityView) {
        this.profileRecentActivityView = profileRecentActivityView
    }
    fun setProfileAnnouncementView(profileAnnouncementView: ProfileAnnouncementView){
        this.profileAnnouncementView = profileAnnouncementView
    }
    fun setProfileAnnouncementDetailView(profileAnnouncementDetailView: ProfileAnnouncementDetailView){
        this.profileAnnouncementDetailView = profileAnnouncementDetailView
    }
    fun setMyInfoView(profileMyInfoView: ProfileMyInfoView) {
        this.profileMyInfoView = profileMyInfoView
    }
    fun setProfileWithdrawalView(profileWithdrawalView: ProfileWithdrawalView) {
        this.profileWithdrawalView = profileWithdrawalView
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
    fun profileAnnouncementSender(){
        val profileDataService = NetworkModule.getInstance()?.create(ProfileRetrofitInterfaces::class.java)
        profileDataService?.getAnnouncementList("Bearer " + getJwt())?.enqueue(object : Callback<ProfileAnnouncementResponse> {
            override fun onResponse(
                call: Call<ProfileAnnouncementResponse>,
                response: Response<ProfileAnnouncementResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp: ProfileAnnouncementResponse = response.body()!!
                    //TODO:SWAGGER업데이트 되면 반영
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
    fun profileAnnouncementDetailSender(announcementId :ProfileAnnouncementDetailRequest){
        val profileDataService = NetworkModule.getInstance()?.create(ProfileRetrofitInterfaces::class.java)
        profileDataService?.getAnnouncementDetail("Bearer " + getJwt(), announcementId)?.enqueue(object : Callback<ProfileAnnouncementDetailResponse> {
            override fun onResponse(call: Call<ProfileAnnouncementDetailResponse>, response: Response<ProfileAnnouncementDetailResponse>) {
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

    // 나의 정보 조회
    fun profileMyInfoSender() {
        val profileMyInfoService = NetworkModule.getInstance()?.create(ProfileMyInfoRetrofitInterfaces::class.java)
        profileMyInfoService?.getMyInfo("Bearer " + getJwt())?.enqueue(object: Callback<ProfileMyInfoResponse> {
            override fun onResponse(call: Call<ProfileMyInfoResponse>, response: Response<ProfileMyInfoResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> profileMyInfoView.onProfileMyInfoSuccess(resp.result)
                        4000 -> Log.d("PROFILE-RESPONSE", "서버 오류")
                        else -> profileMyInfoView.onProfileMyInfoFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<ProfileMyInfoResponse>, t: Throwable) {
                Log.d("PROFILE-RESPONSE", "ProfileDataService-onFailure : getMyInfoFailed", t)
            }
        })
    }

    // 회원 탈퇴
    fun profileWithdrawalSender(id: Int, body: ProfileWithdrawalRequest) {
        val profileWithdrawalService = NetworkModule.getInstance()?.create(ProfileWithdrawalRetrofitInterfaces::class.java)
        profileWithdrawalService?.profileWithdrawal("Bearer " + getJwt(), id, body)?.enqueue(object: Callback<ProfileWithdrawalResponse> {
            override fun onResponse(call: Call<ProfileWithdrawalResponse>, response: Response<ProfileWithdrawalResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> profileWithdrawalView.onProfileWithdrawalSuccess()
                        4000 -> Log.d("PROFILE-RESPONSE", "서버 오류")
                        else -> profileWithdrawalView.onProfileWithdrawalFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<ProfileWithdrawalResponse>, t: Throwable) {
                Log.d("PROFILE-RESPONSE", "ProfileDataService-onFailure : profileWithdrawalFailed", t)
            }
        })
    }
}