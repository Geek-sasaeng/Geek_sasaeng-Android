package com.example.geeksasaeng.ChatSetting

interface WebSocketListenerInterface {
    fun onConnectSuccess () // successfully connected
    fun onConnectFailed () // connection failed
    fun onClose () // close
    fun onMessage(text: String?)
}