package com.example.geeksasaeng.Profile.Retrofit

import android.util.Log
import com.example.geeksasaeng.Signup.Retrofit.SignUpResponse
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import com.example.geeksasaeng.Utils.getJwt
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Path

class ProfileDataService {

    //뷰 객체 생성
    private lateinit var profileMyOngoingActivityView: ProfileMyOngoingActivityView
    private lateinit var profileAnnouncementView: ProfileAnnouncementView
    private lateinit var profileAnnouncementDetailView: ProfileAnnouncementDetailView
    private lateinit var profileMyInfoView: ProfileMyInfoView
    private lateinit var profileMyPreActivityView: ProfileMyPreActivityView
    private lateinit var profileWithdrawalView: ProfileWithdrawalView
    private lateinit var profileMemberInfoModifyView: ProfileMemberInfoModifyView
    private lateinit var profilePasswordCheckingView: ProfilePasswordCheckingView
    private lateinit var profilePasswordChangeView: ProfilePasswordChangeView

    private val profileDataService = NetworkModule.getInstance()?.create(ProfileRetrofitInterfaces::class.java)

    fun setProfileMyOngoingActivityView(profileMyOngoingActivityView: ProfileMyOngoingActivityView) {
        this.profileMyOngoingActivityView = profileMyOngoingActivityView
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
    fun setMyPreActivityView(profileMyPreActivityView: ProfileMyPreActivityView) {
        this.profileMyPreActivityView = profileMyPreActivityView
    }
    fun setProfileWithdrawalView(profileWithdrawalView: ProfileWithdrawalView) {
        this.profileWithdrawalView = profileWithdrawalView
    }
    fun setProfileMemberInfoModifyView(profileMemberInfoModifyView: ProfileMemberInfoModifyView) {
        this.profileMemberInfoModifyView = profileMemberInfoModifyView
    }
    fun setProfilePasswordCheckingView(profilePasswordCheckingView: ProfilePasswordCheckingView) {
        this.profilePasswordCheckingView = profilePasswordCheckingView
    }
    fun setProfilePasswordChangeView(profilePasswordChangeView: ProfilePasswordChangeView) {
        this.profilePasswordChangeView = profilePasswordChangeView
    }

    // 진행 중인 활동 조회
    fun profileMyOngoingActivitySender() {
        profileDataService?.getRecentActivity()?.enqueue(object : Callback<ProfileMyOngoingActivityResponse> {
            override fun onResponse(call: Call<ProfileMyOngoingActivityResponse>, response: Response<ProfileMyOngoingActivityResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp: ProfileMyOngoingActivityResponse = response.body()!!
                    when (resp.code) {
                        1000 -> profileMyOngoingActivityView.onProfileMyOngoingActivitySuccess(resp.result!!)
                        4000 -> Log.d("PROFILE_RECENT_ACTIVITY", "서버 오류")
                        else -> profileMyOngoingActivityView.onProfileMyOngoingActivityFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<ProfileMyOngoingActivityResponse>, t: Throwable) {
                Log.d("PROFILE-RECENT-ACTIVITY", "ProfileDataService-onFailure : getRecentActivity", t)
            }
        })
    }

    // 공지사항 조회
    fun profileAnnouncementSender(){
        profileDataService?.getAnnouncementList()?.enqueue(object : Callback<ProfileAnnouncementResponse> {
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
        profileDataService?.getAnnouncementDetail( announcementId)?.enqueue(object : Callback<ProfileAnnouncementDetailResponse> {
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
        profileDataService?.getMyInfo()?.enqueue(object: Callback<ProfileMyInfoResponse> {
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

    // 진행했던 활동 조회
    fun profileMyPreActivitySender(cursor: Int) {
        profileDataService?.getMyPreActivity(cursor)?.enqueue(object: Callback<ProfileMyPreActivityResponse> {
            override fun onResponse(call: Call<ProfileMyPreActivityResponse>, response: Response<ProfileMyPreActivityResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> profileMyPreActivityView.onProfileMyPreActivityViewSuccess(resp.result)
                        4000 -> Log.d("PROFILE-RESPONSE", "서버 오류")
                        else -> profileMyPreActivityView.onProfileMyPreActivityViewFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<ProfileMyPreActivityResponse>, t: Throwable) {
                Log.d("PROFILE-RESPONSE", "ProfileDataService-onFailure : getMyPreActivityFailed", t)
            }
        })
    }

    // 회원 탈퇴
    fun profileWithdrawalSender(id: Int, body: ProfileWithdrawalRequest) {
        profileDataService?.profileWithdrawal(id, body)?.enqueue(object: Callback<ProfileWithdrawalResponse> {
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

    // 회원 정보 수정
    fun profileMemberInfoModifySender(profileImg: MultipartBody.Part?, data: HashMap<String, RequestBody>) {
        profileDataService?.profileMemberInfoModify(profileImg, data)?.enqueue(object: Callback<ProfileMemberInfoModifyResponse> {
            override fun onResponse(
                call: Call<ProfileMemberInfoModifyResponse>,
                response: Response<ProfileMemberInfoModifyResponse>
            ) {
                Log.d("sendImg/ PROFILE-MODIFY-RESPONSE", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("sendImg/ PROFILE-MODIFY-RESP", resp.toString())
                    when (resp.code) {
                        1000 -> profileMemberInfoModifyView.onProfileMemberInfoModifySuccess(resp.result)
                        4000 -> Log.d("sendImg/ PROFILE-MODIFY-RESPONSE", "서버 오류")
                        else -> profileMemberInfoModifyView.onProfileMemberInfoModifyFailure(resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ProfileMemberInfoModifyResponse>, t: Throwable) {
                Log.d("sendImg/ PROFILE-MODIFY-RESPONSE", "ProfileDataService-onFailure : profileMemberInfoModifyFailed", t)
            }
        })
    }

    // 비밀번호 일치 확인
    fun profilePasswordCheckingSender(body: ProfilePasswordCheckingRequest) {
        profileDataService?.profilePasswordChecking(body)?.enqueue(object: Callback<ProfilePasswordCheckingResponse> {
            override fun onResponse(
                call: Call<ProfilePasswordCheckingResponse>,
                response: Response<ProfilePasswordCheckingResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("PROFILE-PASSWORD-CHECKING-RESPONSE", resp.toString())
                    when (resp.code) {
                        1203 -> profilePasswordCheckingView.onProfilePasswordCheckingSuccess()
                        4000 -> Log.d("PROFILE-PASSWORD-CHECKING-RESPONSE", "서버 오류")
                        else -> profilePasswordCheckingView.onProfilePasswordCheckingFailure(resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ProfilePasswordCheckingResponse>, t: Throwable) {
                Log.d("PROFILE-PASSWORD-CHECKING-RESPONSE", "ProfileDataService-onFailure : profilePasswordCheckingFailed", t)
            }
        })
    }

    // 비밀번호 변경
    fun profilePasswordChangeSender(body: ProfilePasswordChangeRequest) {
        profileDataService?.profilePasswordChange(body)?.enqueue(object: Callback<ProfilePasswordChangeResponse> {

            override fun onResponse(call: Call<ProfilePasswordChangeResponse>, response: Response<ProfilePasswordChangeResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("PROFILE-PASSWORD-CHANGE-RESPONSE", resp.toString())
                    when (resp.code) {
                        1000 -> profilePasswordCheckingView.onProfilePasswordCheckingSuccess()
                        4000 -> Log.d("PROFILE-PASSWORD-CHANGE-RESPONSE", "서버 오류")
                        else -> profilePasswordCheckingView.onProfilePasswordCheckingFailure(resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<ProfilePasswordChangeResponse>, t: Throwable) {
                Log.d("PROFILE-PASSWORD-CHANGE-RESPONSE", "ProfileDataService-onFailure : profilePasswordChangeFailed", t)
            }
        })
    }

}

