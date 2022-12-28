package com.example.geeksasaeng.Chatting.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.ApplicationClass.Companion.retrofit
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChattingDataService {
    private lateinit var matchingEndView: MatchingEndView

    private val chattingDataService = NetworkModule.getInstance()?.create(ChattingRetrofitInterfaces::class.java)

    fun setMatchingEndView(matchingEndView: MatchingEndView){
        this.matchingEndView = matchingEndView
    }

    fun matchingEndSender(roomUuid: String){
        Log.d("matchingEND","Bearer " + getJwt()+"/"+roomUuid.toString())
        chattingDataService?.matchingEnd("Bearer " + getJwt(),roomUuid)?.enqueue(object : Callback<MatchingEndResponse>{
            override fun onResponse(call: Call<MatchingEndResponse>, response: Response<MatchingEndResponse>) {
                Log.d("matchingEND", response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("matchingEND", resp.toString())
                    when(resp.code){
                        1000->matchingEndView.onMatchingEndSuccess()
                        else->matchingEndView.onMatchingEndFailure(resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<MatchingEndResponse>, t: Throwable) {
                Log.d("MATCHINGEND-RESPONSE", "ChattingDataService-onFailure : MatchingEndFailed", t)
            }
        })
    }
}