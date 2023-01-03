package com.example.geeksasaeng.ChatSetting

import android.util.Log
import com.example.geeksasaeng.Chatting.ChattingRoom.ChattingRoomActivity
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class ChatWebSocketListener : WebSocketListener() {

    val chattingRoom = ChattingRoomActivity()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        chattingRoom.webSocketPrint("webSocket : $webSocket")
        chattingRoom.webSocketPrint("response : $response")
        webSocket.send("{\"type\":\"ticker\", \"symbols\": [\"BTC_KRW\"], \"tickTypes\": [\"30M\"]}")
        webSocket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        chattingRoom.webSocketPrint("Receiving : $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        chattingRoom.webSocketPrint("Receiving bytes : $bytes")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        chattingRoom.webSocketPrint("Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        chattingRoom.webSocketPrint("Error : " + t.message)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}