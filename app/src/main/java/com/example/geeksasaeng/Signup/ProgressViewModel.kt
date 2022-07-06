package com.example.geeksasaeng.Signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProgressViewModel: ViewModel() {
    var currentPro: MutableLiveData<Int> = MutableLiveData<Int>(0)

    fun increase() {
        currentPro.setValue(currentPro.getValue()!! + 1)
    }

    fun decrease() {
        currentPro.setValue(currentPro.getValue()!! - 1)
    }

}