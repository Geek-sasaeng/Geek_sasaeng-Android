package com.example.geeksasaeng.Utils

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.mSharedPreferences

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

fun getUuid(): String? = mSharedPreferences.getString("uuid", null)
fun getNickname(): String? = mSharedPreferences.getString("nickname", null)
fun getProfileImgUrl(): String? = mSharedPreferences.getString("profileImgUrl", null)
fun getDormitoryId(): Int? = mSharedPreferences.getInt("dormitoryId", -1)
fun getDormitory(): String? = mSharedPreferences.getString("dormitory", null)
fun getJwt(): String? = mSharedPreferences.getString("jwt", null)
fun isAutoLogin(): Boolean? = mSharedPreferences.getBoolean("autoLogin", false)
fun getLoginId(): String? = mSharedPreferences.getString("loginId", null) // TODO: ?? save가 없는뎅..
fun getPassword(): String? = mSharedPreferences.getString("password", null) // TODO: ??