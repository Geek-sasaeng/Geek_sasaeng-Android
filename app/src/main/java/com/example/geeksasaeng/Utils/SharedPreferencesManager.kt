package com.example.geeksasaeng.Utils

import android.preference.PreferenceManager
import android.util.Log
import com.example.geeksasaeng.Home.Search.DeliverySearchRecent
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.mSharedPreferences
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException


// 닉네임
fun removeNickname() {
    val editor = mSharedPreferences.edit()
    editor.remove("nickname")
    editor.commit()
}

fun saveNickname(nickname: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("nickname", nickname)
    editor.apply()
}

// Member Id
fun removeMemberId() {
    val editor = mSharedPreferences.edit()
    editor.remove("memberId")
    editor.commit()
}

fun saveMemberId(memberId: Int) {
    val editor = mSharedPreferences.edit()
    editor.putInt("memberId", memberId)
    editor.apply()
}

// 프로필 이미지
fun removeProfileImgUrl(){
    val editor = mSharedPreferences.edit()
    editor.remove("profileImgUrl")
    editor.commit()
}

fun saveProfileImgUrl(url: String){
    val editor = mSharedPreferences.edit()
    editor.putString("profileImgUrl", url)
    editor.apply()
}

// 기숙사 정보
fun removeDormitory(){
    val editor = mSharedPreferences.edit()
    editor.remove("dormitory")
    editor.commit()
}

fun saveDormitory(dorm: String){
    val editor = mSharedPreferences.edit()
    editor.putString("dormitory", dorm)
    editor.apply()
}

// 기숙사 ID
fun saveDormitoryId(dormId: Int){
    val editor = mSharedPreferences.edit()
    editor.putInt("dormitoryId", dormId)
    editor.apply()
}

// uuid
fun removeUuid(){
    val editor = mSharedPreferences.edit()
    editor.remove("uuid")
    editor.commit()
}
fun saveUuid(uuid: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("uuid", uuid)
    editor.apply()
}

// isSocial
fun removeIsSocial(){
    val editor = mSharedPreferences.edit()
    editor.remove("isSocial")
    editor.commit()
}
fun saveIsSocial(isSocial: Boolean) {
    val editor = mSharedPreferences.edit()
    editor.putBoolean("isSocial", isSocial)
    editor.apply()
}

// 자동로그인
fun removeAutoLogin() {
    val editor = mSharedPreferences.edit()
    editor.remove("jwt")
    editor.remove("is") //TODO: ??? "is"가 뭐지?
    editor.remove("loginId")
    editor.remove("password")
    editor.remove("autoLogin")
    editor.commit()
}

fun saveJwt(jwt: String){
    Log.d("jwt", jwt)
    val editor = mSharedPreferences.edit()
    editor.putString("jwt", jwt)
    editor.apply()
}

fun setAutoLogin(jwt: String){
    val editor = mSharedPreferences.edit()
    editor.putString("jwt", jwt)
    editor.putBoolean("autoLogin", true)
    editor.apply()
}

// 강제퇴장 리스트
fun removeForcedExitCheckList(){
    val editor = mSharedPreferences.edit()
    editor.remove("forcedExitList")
    editor.commit()
}

fun saveForcedExitCheckList(forcedExitCheckList: MutableList<Boolean>){

    val editor = mSharedPreferences.edit()
    val a = JSONArray()
    for (i in 0 until forcedExitCheckList.size) {
        a.put(forcedExitCheckList[i])
    }
    if (!forcedExitCheckList.isEmpty()) {
        editor.putString("forcedExitList", a.toString())
    } else {
        editor.putString("forcedExitList", null)
    }
    editor.apply()
}

// 최근 검색어
// 최근 검색어를 SharedPreference에 저장하는 함수
fun saveRecentSearch(keyword: String) {
    val editor = mSharedPreferences.edit()

    // 기존 검색어 리스트를 가져옴
    val recentSearches = getRecentSearches()

    // 검색어가 이미 리스트에 존재하는 경우, 해당 검색어를 맨 앞으로 이동시킵니다.
    if (recentSearches.contains(keyword)) {
        recentSearches.remove(keyword)
    }
    recentSearches.add(0, keyword)

    // 최근 검색어 리스트를 JSONArray 형태로 변환하여 SharedPreference에 저장
    val a = JSONArray()
    for (i in 0 until recentSearches.size) {
        a.put(recentSearches[i])
    }

    if (!recentSearches.isEmpty()) {
        editor.putString("recent_searches", a.toString())
    } else {
        editor.putString("recent_searches", null)
    }
    editor.apply()
}

// 최근 검색어를 SharedPreference에서 삭제하는 함수
fun deleteRecentSearch(keyword: String) {
    Log.d("searchFlag", "삭제함수 실행됨 :${keyword}")
    val editor = mSharedPreferences.edit()

    // 기존 검색어 리스트를 가져옴
    val recentSearches = getRecentSearches()
    removeRecentSearch()

    // 검색어를 리스트에서 제거
    recentSearches.remove(keyword)

    Log.d("searchFlag", "삭제함수 실행됨 :${recentSearches}")
    // 최근 검색어 리스트를 JSONArray 형태로 변환하여 SharedPreference에 저장
    val a = JSONArray()
    for (i in 0 until recentSearches.size) {
        a.put(recentSearches[i])
    }

    if (!recentSearches.isEmpty()) {
        editor.putString("recent_searches", a.toString())
    } else {
        editor.putString("recent_searches", null)
    }
    editor.apply()
}

fun removeRecentSearch(){
    val editor = mSharedPreferences.edit()
    editor.remove("recent_searches")
    editor.commit()
}


fun getUuid(): String? = mSharedPreferences.getString("uuid", null)
fun getIsSocial(): Boolean? = mSharedPreferences.getBoolean("isSocial", false)
fun getNickname(): String? = mSharedPreferences.getString("nickname", null)
fun getMemberId(): Int? = mSharedPreferences.getInt("memberId", 0)
fun getProfileImgUrl(): String? = mSharedPreferences.getString("profileImgUrl", null)
fun getDormitoryId(): Int = mSharedPreferences.getInt("dormitoryId", -1)
fun getDormitory(): String? = mSharedPreferences.getString("dormitory", null)
fun getJwt(): String? = mSharedPreferences.getString("jwt", null)
fun isAutoLogin(): Boolean? = mSharedPreferences.getBoolean("autoLogin", false)
fun getLoginId(): String? = mSharedPreferences.getString("loginId", null) // TODO: ?? save가 없는뎅..
fun getPassword(): String? = mSharedPreferences.getString("password", null) // TODO: ??
fun getForcedExitCheckList(): MutableList<Boolean> {
    val json = mSharedPreferences.getString("forcedExitList", null)
    val forcedExitCheckList = ArrayList<Boolean>()
    if (json != null) {
        try {
            val a = JSONArray(json)
            for (i in 0 until a.length()) {
                val check = a.optString(i)
                forcedExitCheckList.add(check.toBoolean())
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    return forcedExitCheckList
}
fun getRecentSearches(): MutableList<String> {
    val json = mSharedPreferences.getString("recent_searches", null)
    val recentSearches = ArrayList<String>()
    if (json != null) {
        try {
            val a = JSONArray(json)
            for (i in 0 until a.length()) {
                val keyword = a.optString(i)
                recentSearches.add(keyword.toString())
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    Log.d("searchFlag", "불러오기함수 실행됨 :${recentSearches}")
    return recentSearches
}
