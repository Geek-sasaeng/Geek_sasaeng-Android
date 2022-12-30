package com.example.geeksasaeng.ChatSetting

import android.widget.Toast
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.net.SocketException

class WebSocket : Thread() {
//    override fun run() {
//        val hostName = "localhost"
//        val port = 10001
//
//        try {
//            val socket = Socket(hostName, port)
//
//            val outputStream = ObjectOutputStream(socket.getOutputStream())
//            outputStream.writeObject(editText.text.toString())
//            outputStream.flush()
//
//            val inputStream = ObjectInputStream(socket.getInputStream())
//            val input = inputStream.readObject() as String
//
//            handler.post {
//                textView.append("$input\n")
//            }
//        } catch (e: SocketException) {
//            handler.post {
//                Toast.makeText(applicationContext, "소켓 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}