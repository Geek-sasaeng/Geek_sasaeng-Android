package com.example.geeksasaeng.Signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProgressViewModel: ViewModel() {
    var currentPro = MutableLiveData<Int>()

    fun getCurrentPro(): MutableLiveData<Int> {
        return currentPro
    }
}