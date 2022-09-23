package com.example.geeksasaeng.Login.Retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH

interface DormitoryRetrofitInterfaces {
    //기숙사 수정하기
    @PATCH("/members/dormitory")
    fun dormitoryUpdate(
        @Body dormitoryId: DormitoryRequest
    ): Call<DormitoryResponse>
}
