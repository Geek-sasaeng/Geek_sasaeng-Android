package com.example.geeksasaeng.Utils

import androidx.appcompat.app.AppCompatActivity
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.mSharedPreferences

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

fun getUuid(): String? = mSharedPreferences.getString("uuid", null)

// 자동로그인
fun removeAutoLogin() {
    val editor = mSharedPreferences.edit()
    editor.remove("jwt")
    editor.remove("is")
    editor.remove("loginId")
    editor.remove("password")
    editor.remove("autoLogin")
    editor.commit()
}

fun saveJwt(jwt: String){
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

fun getDormitory(): String? = mSharedPreferences.getString("dormitory", null)
fun getJwt(): String? = mSharedPreferences.getString("jwt", null)
fun isAutoLogin(): Boolean? = mSharedPreferences.getBoolean("autoLogin", false)
fun getLoginId(): String? = mSharedPreferences.getString("loginId", null)
fun getPassword(): String? = mSharedPreferences.getString("password", null)