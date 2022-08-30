package com.example.geeksasaeng.Signup.Basic

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProgressViewModel: ViewModel() {
    var currentPro: MutableLiveData<Int> = MutableLiveData<Int>()

    init {
        currentPro.value = 0
    }

    fun increase() {
        currentPro.value = currentPro.value!! + 1
        Log.d("PROGRESS-STATUS", "CURRENTPRO = " + currentPro.value.toString())
    }

    fun setValue(num:Int){
        currentPro.value = num
    }

    // TODO: 회원가입 Dialog 뒤로가기 부분에서 사용!!
    fun decrease() {
        currentPro.value = currentPro.value!! - 1
    }
}