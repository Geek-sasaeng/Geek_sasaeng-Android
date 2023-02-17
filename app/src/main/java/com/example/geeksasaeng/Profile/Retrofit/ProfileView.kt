package com.example.geeksasaeng.Profile.Retrofit

// 진행 중인 활동 조회
interface ProfileMyOngoingActivityView {
    fun onProfileMyOngoingActivitySuccess(result: ArrayList<ProfileMyOngoingActivityResult>?)
    fun onProfileMyOngoingActivityFailure(message: String)
}

// 공지사항 전체 조회
interface ProfileAnnouncementView {
    fun onProfileAnnouncementSuccess(result: ArrayList<ProfileAnnouncementResult>)
    fun onProfileAnnouncementFailure(message: String)
}

// 공지사항 상세 조회
interface ProfileAnnouncementDetailView{
    fun onProfileAnnouncementDetailSuccess(result: ProfileAnnouncementDetailResult)
    fun onProfileAnnouncementDetailFailure(message: String)
}

// 내 정보 조회
interface ProfileMyInfoView {
    fun onProfileMyInfoSuccess(result: ProfileMyInfoResult)
    fun onProfileMyInfoFailure(message: String)
}

// 진행했던 활동 조회
interface ProfileMyPreActivityView {
    fun onProfileMyPreActivityViewSuccess(result: ProfileMyPreActivityResult)
    fun onProfileMyPreActivityViewFailure(message: String)
}

// 회원 탈퇴
interface ProfileWithdrawalView {
    fun onProfileWithdrawalSuccess()
    fun onProfileWithdrawalFailure(message: String)
}

// 회원 정보 수정
interface ProfileMemberInfoModifyView {
    abstract val contentResolver: Any
    fun onProfileMemberInfoModifySuccess(result: ProfileMemberInfoModifyResult)
    fun onProfileMemberInfoModifyFailure(message: String)
}

// 비밀번호 일치 확인
interface ProfilePasswordCheckingView {
    fun onProfilePasswordCheckingSuccess()
    fun onProfilePasswordCheckingFailure(message: String)
}

// 비밀번호 수정
interface ProfilePasswordChangeView {
    fun onProfilePasswordChangeSuccess()
    fun onProfilePasswordChangeFailure(message: String)
}

// 대학교별 기숙사 목록 조회
interface ProfileViewDormitoryView {
    fun onProfileViewDormitorySuccess(result: ArrayList<ProfileViewDormitoryResult>)
    fun onProfileViewDormitoryFailure(message: String)
}