package com.example.geeksasaeng.Chatting.ChattingList

import android.util.Log
import com.example.geeksasaeng.Utils.getNickname
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FirestoreChatData(
    val roomUuid: String,
    val position: Int,
    val chattingList: ArrayList<ChattingData>
) {
    private val db = Firebase.firestore //파이어스토어
    private lateinit var chatDataView: ChatDataView
    var cnt: Int = 0

    fun setChatDataView(chatDataView: ChatDataView) {
        this.chatDataView = chatDataView
    }

    fun getLastChatData() {
        db.collection("Rooms").document(roomUuid).collection("Messages")
            .orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, _ ->
                val chattingData = chattingList.get(position)
                for ((idx, doc) in snapshots?.documentChanges!!.withIndex()) {
                    if (idx == 0) { // 가장 최근에 보낸 메시지, 시간 가져오기
                        val lastChat = doc.document["content"].toString()
                        val lastMsgTime = doc.document["time"].toString()
                        val lastTime = getLastTimeString(lastMsgTime!!)

                        chattingData.lastChat = lastChat
                        chattingData.lastMsgTime = lastTime
                    }
                    if (doc.document["readUsers"] != null) {
                        val readUsers = doc.document["readUsers"] as ArrayList<String>
                        if (!readUsers.contains(getNickname())) {
                            cnt++
                        }
                    }
                }
                if (cnt == 0) {
                    chattingData.newMsg = ""
                } else {
                    chattingData.newMsg = "+$cnt"
                }
                chatDataView.onSuccessGetChatData(position, chattingData)
            }
    }
}

private fun getLastTimeString(lastMsgTime: String): String {
    var msgYear = Integer.parseInt(lastMsgTime.substring(0, 4))
    var msgMonth = Integer.parseInt(lastMsgTime.substring(5, 7))
    var msgDay = Integer.parseInt(lastMsgTime.substring(8, 10))
    var msgHours = Integer.parseInt(lastMsgTime.substring(11, 13))
    var msgMinutes = Integer.parseInt(lastMsgTime.substring(14, 16))

    var currentTime = getCurrentDateTime()
    Log.d("cherrycurrentTime", getCurrentDateTime().toString())
    var currentYear = Integer.parseInt(currentTime.substring(0, 4))
    var currentMonth = Integer.parseInt(currentTime.substring(5, 7))
    var currentDay = Integer.parseInt(currentTime.substring(8, 10))
    var currentHours = Integer.parseInt(currentTime.substring(11, 13))
    var currentMinutes = Integer.parseInt(currentTime.substring(14, 16))

    var currentTotalSec = Calendar.getInstance().apply {
        set(Calendar.YEAR, currentYear)
        set(Calendar.MONTH, currentMonth)
        set(Calendar.DAY_OF_MONTH, currentDay)
    }.timeInMillis + (60000 * 60 * currentHours) + (60000 * currentMinutes)

    var lastMsgTotalSec = Calendar.getInstance().apply {
        set(Calendar.YEAR, msgYear)
        set(Calendar.MONTH, msgMonth)
        set(Calendar.DAY_OF_MONTH, msgDay)
    }.timeInMillis + (60000 * 60 * msgHours) + (60000 * msgMinutes)

    var totalDayCurrent = currentTotalSec / (24 * 60 * 60 * 1000) //하루 단위로 계산
    var totalDayMsg = lastMsgTotalSec / (24 * 60 * 60 * 1000) //하루 단위로 계산
    var dateGap = (totalDayCurrent - totalDayMsg).toInt()

    ///* 포맷팅 기준에 따라 최근 메세지의 전송 시간 설정 */

    if (dateGap == 0) { // 오늘 보낸 문자일때 (하루가 안지났으면)

        var passedTime = (currentTotalSec - lastMsgTotalSec) / (1000 * 60) //경과시간 측정 (분단위)

        if (passedTime <= 10) { // 방금
            return "방금"
        } else if (passedTime <= 60) { // n분 전
            return "${passedTime}분전"
        } else { // 그 외에는 시간 단위로
            return "${passedTime / 60}시간 전"
        }
    } else if (dateGap <= 1) { //하루보다 작으면
        return "어제"
    } else if (dateGap <= 3) {
        return "${dateGap}일 전"
    } else { // 3일 이후일때는 날짜로 표시 ex)22-08-15
        return lastMsgTime.substring(2)
    }
}

fun getCurrentDateTime(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(Calendar.getInstance().time)
}
