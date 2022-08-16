package com.example.geeksasaeng.Chatting.ChattingList

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Chatting.ChattingRoom.ChattingRoomActivity
import com.example.geeksasaeng.Chatting.ChattingRoom.getCurrentDateTime
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.FragmentChattingBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChattingFragment: BaseFragment<FragmentChattingBinding>(FragmentChattingBinding::inflate) {

    lateinit var chattingListRVAdapter : ChattingListRVAdapter
    private var chattingList = ArrayList<ChattingListData>()
    private val db = Firebase.firestore //파이어스토어


    override fun onResume() {
        super.onResume()
        initChattingList()
        initAdapter()
    }

    override fun initAfterBinding() {

    }


    private fun initChattingList() {

        Log.d("firestore", "initChattingList => clear 작동됨")
        chattingList.clear() // ChattingRoomActivity 들어갔다가 나오면 방 하나더 추가되는 문제 해결 위해 clear한 후 추가해주는 방식으로 바꿈
        Log.d("firestore", "채팅리스트" + chattingList.toString())
        //TODO: 이부분이 가져와야하는 정보가 많아서,, 구조가 복잡해졌어 - 이 코드가 뭘 의미하는지 모르겠다 하는거 있으면 언제든 제로한테 물어봐줘여
        db.collection("Rooms")
            .whereEqualTo("roomInfo.category", "배달파티")
            .whereEqualTo("roomInfo.isFinish", false)
            //.orderBy("updatedAt")
            .get()
            .addOnSuccessListener { documents -> //배달파티 카테고리고, 종료되지 않은 채팅방 데이터만 가져와 최신 메세지순으로 정렬 성공하면
                for (document in documents) {
                    val roomInfo =
                        document.data.getValue("roomInfo") as HashMap<String, Any> //roomInfo 필드 값 정보들을 해시맵 형태로 얻어온다.

                    val participants =
                        roomInfo.getValue("participants") as ArrayList<HashMap<String, Any>> //roomInfo의 participants를 HashMap형태의 ArrayList로 얻어온다 (//ParticipantInfo로 받고 싶었는데 HashMap을 ParticipantInfo로 cast를 못한뎅,,)
                    for (member in participants) {

                        if (member.getValue("participant") == getNickname()) { // 그 중에 '내가 속하는' 채팅방 정보만 가져온다!

                            val roomUuid = document.id
                            val roomName = roomInfo.getValue("title").toString()

                            var lastChat: String = ""
                            var lastTime: String = ""
                            var lastMsgTime: String? = null

                            db.collection("Rooms").document(roomUuid).collection("Messages")
                                .orderBy("time", Query.Direction.DESCENDING)
                                .limit(1)
                                .get()
                                .addOnSuccessListener { doc -> // 메세지를 time기준 내림차순으로 정렬에 성공했다면
                                    Log.d("tnstj", "채팅 내용, 시간 가져오기 성공")
                                    //마지막 채팅 내용과 마지막 채팅 시간을 가져온다.
                                    if (!doc.isEmpty) {
                                        lastChat =
                                            doc.documents.last().data!!.getValue("content")
                                                .toString()
                                        lastMsgTime =
                                            doc.documents.last().data!!.getValue("time")
                                                .toString()
                                        lastTime = getLastTimeString(lastMsgTime!!)
                                        Log.d("last", lastChat + "/" + lastTime)
                                    }
                                    //firestore에 위에서 정보를 이용하여 chattingList에 추가해준다.
                                    chattingList.add(
                                        ChattingListData(
                                            roomName,
                                            roomUuid,
                                            "http://geeksasaeng.shop/s3/neo.jpg",
                                            lastChat,
                                            lastTime,
                                            "+10",
                                            roomInfo.getValue("updatedAt").toString()
                                        )
                                    )
                                    //TODO: newMsg를 어떻게 카운트 할지가 애매하네....
                                    initAdapter() // add될때마다 initAdapter안하고 chattingList다 만들고 initAdapter하고 싶은데,, 이게 여기에 두고 싶지 않은데,,, 이방법 밖에 모르겠어
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(
                                        "firestore",
                                        "Error getting documents: 최근메세지 불러오기 실패 ",
                                        exception
                                    )
                                }

                        }
                    }
                }
                initAdapter()
            }
            .addOnFailureListener { exception ->
                Log.d("firestore", "Error getting documents: ", exception)
            }
    }

    private fun getLastTimeString(lastMsgTime: String): String { //getRemainTime이랑 같은 비슷한
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

        var TotalDayCurrent = currentTotalSec / (24*60*60*1000) //하루 단위로 계산
        var TotalDayMsg = lastMsgTotalSec/ (24*60*60*1000) //하루 단위로 계산
        var DateGap = (TotalDayCurrent -TotalDayMsg).toInt()

        ///* 포맷팅 기준에 따라 최근 메세지의 전송 시간 설정 */

        if( DateGap == 0){ // 오늘 보낸 문자일때 (하루가 안지났으면)

            var passedTime = (currentTotalSec - lastMsgTotalSec)/ (1000*60) //경과시간 측정 (분단위)

            if(passedTime<=10){ //10분 이내이면 방금
                return "방금"
            }else if(passedTime<=60){ //1시간 이내이면 n분 전
                return "${passedTime}분전"
            }else{ // 그 외에는 시간 단위로
                return "${passedTime/60}시간 전"
            }
        } else if(DateGap <= 1){ //하루보다 작으면
            return "어제"
        } else if (DateGap <= 3){
            return "${currentTime.toInt() -lastMsgTime.toInt()}일 전"
        } else { // 3일 이후일때는 날짜로 표시 ex)22-08-15
            return lastMsgTime.substring(2)
        }
    }

    fun getCurrentDateTime(): String{
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    private fun initAdapter() {

        chattingListRVAdapter = ChattingListRVAdapter(chattingList)
        chattingListRVAdapter.itemSort()
        Log.d("firestore", "어댑터채팅리스트"+ chattingList.toString())
        binding.chattingListRv.adapter = chattingListRVAdapter
        binding.chattingListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        chattingListRVAdapter.setOnItemClickListener(object:
            ChattingListRVAdapter.OnItemClickListener {
            override fun onItemClick(chattingList: ChattingListData, position: Int) { //채팅방 입장할때
                val intent = Intent(activity, ChattingRoomActivity::class.java)
                intent.putExtra("roomName", chattingList.roomName)
                intent.putExtra("roomUuid", chattingList.roomUuid)
                startActivity(intent)
            }
        })
    }

}