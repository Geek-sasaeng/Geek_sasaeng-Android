package com.example.geeksasaeng.Signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProgressViewModel: ViewModel() {
    var currentPro: MutableLiveData<Int> = MutableLiveData<Int>()

    init {
        currentPro.value = 0
    }

    fun setPro(progress: Int) {
        currentPro.value = currentPro.value?.plus(1)
    }

    fun increase() {
        currentPro.value = currentPro.value!! + 1
        Log.d("PROGRESS-STATUS", "CURRENTPRO = " + currentPro.value.toString())
    }

    fun decrease() {
        currentPro.value = currentPro.value!! - 1
    }

}