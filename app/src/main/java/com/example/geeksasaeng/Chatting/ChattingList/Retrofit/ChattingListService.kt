package com.example.geeksasaeng.Chatting.ChattingList.Retrofit

import android.util.Log
import com.example.geeksasaeng.Utils.NetworkModule
import com.example.geeksasaeng.Utils.getJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingListService {
    private lateinit var getChattingListView: ChattingListView
    private lateinit var getChattingDetailView: ChattingDetailView

    private val chattingListService = NetworkModule.getInstance()?.create(ChattingListRetrofitInterfaces::class.java)

    fun setChattingListView(getChattingListView: ChattingListView) {
        this.getChattingListView = getChattingListView
    }
    fun setChattingDetailView(getChattingDetailView: ChattingDetailView) {
        this.getChattingDetailView = getChattingDetailView
    }

    fun getChattingList(cursor: Int) {
        chattingListService?.getChattingList(cursor)?.enqueue(object : Callback<ChattingListResponse> {
            override fun onResponse(call: Call<ChattingListResponse>, response: Response<ChattingListResponse>) {
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

    fun getChattingDetail(chatRoomId: String) {
        chattingListService?.getChattingDetail(chatRoomId)?.enqueue(object: Callback<ChattingDetailResponse> {
            override fun onResponse(call: Call<ChattingDetailResponse>, response: Response<ChattingDetailResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    when (resp.code) {
                        1000 -> getChattingDetailView.getChattingDetailSuccess(resp.result)
                        else -> getChattingDetailView.getChattingDetailFailure(resp.code, resp.message)
                    }
                }
            }
            override fun onFailure(call: Call<ChattingDetailResponse>, t: Throwable) {
                Log.d("RETROFIT-SERVICE", "ChattingListService-getChattingDetail-Failure")
            }
        })
    }
}