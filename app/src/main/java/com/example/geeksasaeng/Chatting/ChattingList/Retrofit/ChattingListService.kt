package com.example.geeksasaeng.Chatting.ChattingList.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingListService {
    private lateinit var getChattingListView: ChattingListView

    private val chattingListService = NetworkModule.getInstance()?.create(
        ChattingListRetrofitInterfaces::class.java)

    fun setChattingListView(getChattingListView: ChattingListView) {
        this.getChattingListView = getChattingListView
    }

    fun getChattingList(cursor: Int) {
        chattingListService?.getChattingList(cursor)?.enqueue(object : Callback<ChattingListResponse> {
            override fun onResponse(call: Call<ChattingListResponse>, response: Response<ChattingListResponse>) {
                Log.d("chattingList", "cursor: "+cursor.toString()+"response : "+response.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> getChattingListView.getChattingListSuccess(resp.result)
                        else -> getChattingListView.getChattingListFailure(resp.code, resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<ChattingListResponse>, t: Throwable) {
                Log.d("RETROFIT-SERVICE", "ChattingListService-getChattingList-Failure")
            }
        })
    }
}