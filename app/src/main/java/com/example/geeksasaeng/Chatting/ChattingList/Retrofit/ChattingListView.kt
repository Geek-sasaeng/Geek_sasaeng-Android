package com.example.geeksasaeng.Chatting.ChattingList.Retrofit

interface ChattingListView {
    fun getChattingListSuccess(result: ChattingListResult)
    fun getChattingListFailure(code: Int, msg: String)
}