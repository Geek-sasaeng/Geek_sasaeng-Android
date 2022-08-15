package com.example.geeksasaeng.Utils

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList
import java.util.HashMap

// 나의 participant Map 가져오기
fun getMyParticipantMap(roomUuid: String): HashMap<String, String>? {
    val db = FirebaseFirestore.getInstance()
    var participant: HashMap<String, String>? = null
    // 채팅방 참여자 hashMap 가져오기
    db.collection("Rooms")
        .document(roomUuid)
        .get()
        .addOnSuccessListener { document ->
            val roomInfo =
                document.get("roomInfo") as java.util.HashMap<String, Any> //roomInfo 필드 값 정보들을 해시맵 형태로 얻어온다.
            val participants = roomInfo.get("participants") as ArrayList<Any>
            for ((idx, map) in participants.withIndex()) {
                val map = map as HashMap<String, String>
                val participantName = map.get("participant").toString()
                if (participantName.equals(getNickname())) {
                    participant = map;
                    break
                }
            }
        }
    return participant
}

// 채팅방 참여자 삭제
fun deleteMyParticipantMap(roomUuid: String, participant: HashMap<String, String>) {
    val db = FirebaseFirestore.getInstance()
    db.collection("Rooms")
        .document(roomUuid)
        .update("roomInfo.participants", FieldValue.arrayRemove(participant))
        .addOnSuccessListener { Log.d("chatting-member-leave", "파이어베이스 채팅방에서 유저가 삭제됐습니다.") }
        .addOnFailureListener { e ->
            Log.w(
                "chatting-member-leave",
                "파이어베이스 채팅방에서 유저를 삭제하는 도중에 오류가 발생했습니다.",
                e
            )
        }
}

// 방장인지 아닌지 확인해주는 메서드
fun checkLeader(roomUuid: String): Boolean {
    val participants = getMyParticipantMap(roomUuid) as ArrayList<Any>
    if(participants == null)
        return false;

    var participantIdx: Int = -1
    for ((idx, map) in participants.withIndex()) {
        val map = map as HashMap<String, String>
        val participantName = map.get("participant").toString()
        if (participantName.equals(getNickname())) {
            participantIdx = idx
            break
        }
    }
    return if (participantIdx == 0) true else false
}