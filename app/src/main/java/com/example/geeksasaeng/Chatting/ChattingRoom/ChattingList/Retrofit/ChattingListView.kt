package com.example.geeksasaeng.Chatting.ChattingRoom.ChattingList.Retrofit

interface ChattingListView {
    fun getChattingListSuccess(result: ChattingListResult)
    fun getChattingListFailure(code: Int, msg: String)
}