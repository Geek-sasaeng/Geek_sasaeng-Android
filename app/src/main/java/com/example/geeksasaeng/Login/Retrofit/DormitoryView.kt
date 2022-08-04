package com.example.geeksasaeng.Login.Retrofit

interface DormitoryView {
    fun onDormitySuccess(result : DormitoryResult)
    fun onDormityFailure(message: String)
}