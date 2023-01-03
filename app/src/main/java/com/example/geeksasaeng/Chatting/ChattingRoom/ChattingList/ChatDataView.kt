package com.example.geeksasaeng.Chatting.ChattingRoom.ChattingList

interface ChatDataView {
    fun onSuccessGetChatData(position: Int, chattingData: ChattingData)
}