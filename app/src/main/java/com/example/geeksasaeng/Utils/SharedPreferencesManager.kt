package com.example.geeksasaeng.Utils

import android.util.Log
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.mSharedPreferences
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

// 이메일
fun removeEmail() {
    val editor = mSharedPreferences.edit()
    editor.remove("email")
    editor.commit()
}

fun saveEmail(email: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("email", email)
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


fun getUuid(): String? = mSharedPreferences.getString("uuid", null)
fun getIsSocial(): Boolean? = mSharedPreferences.getBoolean("isSocial", false)
fun getNickname(): String? = mSharedPreferences.getString("nickname", null)
fun getMemberId(): Int? = mSharedPreferences.getInt("memberId", 0)
fun getProfileImgUrl(): String? = mSharedPreferences.getString("profileImgUrl", null)
fun getDormitoryId(): Int = mSharedPreferences.getInt("dormitoryId", -1)
fun getDormitory(): String? = mSharedPreferences.getString("dormitory", null)
fun getEmail(): String? = mSharedPreferences.getString("email", null)
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
