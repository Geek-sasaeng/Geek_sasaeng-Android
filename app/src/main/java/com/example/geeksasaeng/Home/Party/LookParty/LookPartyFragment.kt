package com.example.geeksasaeng.Home.Party.LookParty

import android.animation.Animator
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.geeksasaeng.Chatting.ChattingList.ParticipantsInfo
import com.example.geeksasaeng.Chatting.ChattingRoom.ChattingRoomActivity
import com.example.geeksasaeng.Home.Party.Retrofit.*
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.getNickname
import com.example.geeksasaeng.databinding.FragmentLookPartyBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.timer

class LookPartyFragment: BaseFragment<FragmentLookPartyBinding>(FragmentLookPartyBinding::inflate), PartyDetailView,
    DialogDeliveryOptionMyPopup.PopUpdateClickListener, DialogPartyRequestView, DialogPartyRequestCompleteView {

    lateinit var loadingAnimationView: LottieAnimationView
    private var deliveryItemId: Int? = null
    private var status: String? = null
    private var authorStatus: Boolean? = null
    private var belongStatus: String? = null
    private var dialogPartyRequest: DialogPartyRequest? = null
    private var remainTime : Long = 0
    private var timerTask : Timer? = null
    private val db = Firebase.firestore //파이어스토어

    lateinit var partyData: PartyDetailResult
    lateinit var mapView : MapView
    lateinit var partyDataService : PartyDataService

    override fun initAfterBinding() {
        initClickListener()
        // binding.lookPartyProgressBar.visibility = View.VISIBLE
        loadingStart()
        binding.lookLocateText.isSelected = true // 물흐르는 애니메이션

        // 파티 수정하기, 신고하기 Stack에서 제거
        (context as MainActivity).supportFragmentManager.popBackStack("partyUpdate", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        (context as MainActivity).supportFragmentManager.popBackStack("partyReport", FragmentManager.POP_BACK_STACK_INCLUSIVE)

        deliveryItemId = Integer.parseInt(arguments?.getString("deliveryItemId"))
        status = arguments?.getString("status")
        val handler = Handler()
        handler.postDelayed({
            initDeliveryPartyData()
        }, 200)
    }

    override fun onStop() {
        super.onStop()
        timerTask?.cancel()
        if (status == "search")
            requireActivity().finish()
    }

    private fun initClickListener(){
        binding.lookPartyBackBtn.setOnClickListener {
            if(status == "lookParty"){
                (context as MainActivity).supportFragmentManager.beginTransaction().remove(this).commit();
                (context as MainActivity).onBackPressed()
            }else{
                (context as MainActivity).supportFragmentManager.beginTransaction().remove(this).commit();
                (context as MainActivity).supportFragmentManager.popBackStack();
            }
        }

        binding.lookPartyOptionBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("partyId", deliveryItemId!!)
            var dialogFragment = DialogFragment()
            var dialogTag = String()
            if (authorStatus == true) {
                bundle.putBoolean("authorStatus", partyData.authorStatus)
                bundle.putString("chief", partyData.chief)
                bundle.putInt("chiefId", partyData.chiefId)
                bundle.putString("chiefProfileImgUrl", partyData.chiefProfileImgUrl)
                bundle.putString("content", partyData.content)
                bundle.putInt("currentMatching", partyData.currentMatching)
                bundle.putInt("dormitory", partyData.dormitory)
                bundle.putString("foodCategory", partyData.foodCategory)
                bundle.putBoolean("hashTag", partyData.hashTag)
                bundle.putInt("id", partyData.id)
                bundle.putDouble("latitude", partyData.latitude)
                bundle.putDouble("longitude", partyData.longitude)
                bundle.putString("matchingStatus", partyData.matchingStatus)
                bundle.putInt("maxMatching", partyData.maxMatching)
                bundle.putString("orderTime", partyData.orderTime)
                bundle.putString("storeUrl", partyData.storeUrl)
                bundle.putString("title", partyData.title)
                bundle.putString("updatedAt", partyData.updatedAt)
                dialogFragment = DialogDeliveryOptionMyPopup()
                dialogTag = "DeliveryPartyMyOption"
            }
            else if (authorStatus == false) {
                // TODO: 게시글 쓴 사람 ID 전달
                bundle.putInt("reportedMemberId", partyData.chiefId)
                bundle.putInt("reportedDeliveryPartyId", partyData.id)
                dialogFragment = DialogDeliveryOptionOtherPopup()
                dialogTag = "DeliveryPartyOtherOption"
            }

            dialogFragment.setArguments(bundle)
            dialogFragment.show(childFragmentManager, dialogTag) // parent->child로 바꿈
        }

        // 매칭 신청 or 채팅방 가기 버튼 누를경우
        binding.lookPartyRequestTv.setOnClickListener {
            if (authorStatus == true || belongStatus == "Y") {
                showPartyChattingRoom()
            } else {
                dialogPartyRequest = DialogPartyRequest(partyData.id)
                dialogPartyRequest!!.setChattingRoonJoinView(this)
                dialogPartyRequest!!.show(parentFragmentManager, "partyRequest")
            }
        }
    }

    private fun initDeliveryPartyData() {
        partyDataService = PartyDataService()
        partyDataService.partyDetailSender(deliveryItemId!!)
        partyDataService.setPartyDetailView(this)
    }

    //상세보기 api 성공시
    override fun partyDetailSuccess(result: PartyDetailResult) {
        partyData = result

        binding.lookPartyWhite.visibility = View.GONE
        // binding.lookPartyProgressBar.visibility = View.GONE
        loadingStop()

        authorStatus = result.authorStatus
        belongStatus = result.belongStatus

        // 글쓴이, 파티 멤버 -> 채팅방 가기로 초기 설정   |   일반 유저 -> 신청하기로 초기 설정
        if (authorStatus == true || belongStatus == "Y") binding.lookPartyRequestTv.text = "채팅방 가기"
        else binding.lookPartyRequestTv.text = "신청하기"

        if (result.chiefProfileImgUrl != null)
            //binding.lookHostProfile.setImageURI(Uri.parse(result.chiefProfileImgUrl))
            Glide.with(this)
                .load(result.chiefProfileImgUrl)
                .into(binding.lookHostProfileIv)
        else // TODO: 기본 이미지 넣을 예정
            binding.lookHostProfileIv.setImageResource(R.drawable.ic_default_profile)

        binding.lookHostName.text = result.chief
        binding.lookContent.text = result.content
        binding.lookMatchingNumber.text = result.currentMatching.toString() + "/" + result.maxMatching
        binding.lookCategoryText.text = result.foodCategory

        if (result.hashTag)
            binding.lookHashTag.visibility = View.VISIBLE
        else
            binding.lookHashTag.visibility = View.GONE

        // 위도, 경도로 수령장소 문자 넣어주기 + 밑에 카카오지도 그리기
        binding.lookLocateText.text = getAddress(result.latitude,  result.longitude)
        val mapPoint = MapPoint.mapPointWithGeoCoord(result.latitude,result.longitude)
        drawMap(mapPoint)


        binding.lookTimeDate.text = "${result.orderTime.substring(5, 7)}월 ${result.orderTime.substring(8, 10)}일"
        binding.lookTimeTime.text = "${result.orderTime.substring(11, 13)}시 ${result.orderTime.substring(14, 16)}분"

        if (result.storeUrl.toString() != "null")
            binding.lookLinkText.text = result.storeUrl
        else {
            binding.lookLink.visibility = View.GONE
            binding.lookLinkText.visibility = View.GONE
        }

        binding.lookTitle.text = result.title
        binding.lookPostDate.text = "${result.updatedAt.substring(5, 7)}/${result.updatedAt.substring(8, 10)} ${result.updatedAt.substring(11, 16)}"

        //밑에 파란 바
        binding.lookMemberStatus.text = result.currentMatching.toString()+"/"+result.maxMatching.toString()+ " 명"
        remainTime = getRemainTime(result.orderTime)
        startTimer()
    }

    override fun partyDetailFailure(code: Int, message: String) {
        showToast(message)
    }

    //위도 경도로 주소 구하는 Reverse-GeoCoding (주소 String으로 반환 받을 수 있다)
    private fun getAddress(latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(context, Locale.KOREA)
        var addr = "주소 오류"

        try {
            addr = geoCoder.getFromLocation(latitude, longitude, 1).first().getAddressLine(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addr
    }

    //맵 그리는 함수
    private fun drawMap(mapPoint: MapPoint){
        mapView = MapView(requireActivity())
        mapView.setOnTouchListener { v, event ->
            binding.lookPartySv.requestDisallowInterceptTouchEvent(true) //부모에게 Touch Event를 빼앗기지 않게 할 수 있다.
            return@setOnTouchListener false
        }
        binding.lookKakaoMapLocation.addView(mapView)
        //마커생성
        val marker = MapPOIItem()
        marker.itemName = "요기?"
        marker.isShowCalloutBalloonOnTouch = false // 클릭시 말풍선을 보여줄지 여부 (false)
        marker.mapPoint = mapPoint
        mapView.addPOIItem(marker)
        mapView!!.setMapCenterPoint(mapPoint, true)//지도 중심점 변경
    }

    override fun onPopUpdateClicked() { // 수정하기 팝업 클릭하면,
        binding.lookKakaoMapLocation.removeView(mapView) // 다른 프레그먼트 띄우기 전에 맵 사용해야하니까 지우기
    }

    // 타이머 작동
    private fun startTimer() {
        timerTask = timer(period = 1000) { //1초가 주기
            val timeForm = DecimalFormat("00") //0을 넣은 곳은 빈자리일 경우, 0으로 채워준다.
            var remainSec = (remainTime) / 1000
            var longHour = (remainSec / 3600)
            var longMinute = ((remainSec % 3600) / 60)
            var longSec = remainSec % 60


            val hour = timeForm.format(longHour)
            val min = timeForm.format(longMinute)
            val sec = timeForm.format(longSec)

            activity?.runOnUiThread {
                binding.lookTimer.text = "${hour}:${min}:${sec}"

                if (min == "00" && sec == "00"){
                    timerTask?.cancel()
                    binding.lookTimer.text = "시간이 만료되었습니다."
                }
            }

            if (remainTime.toInt() != 0) // time이 0이 아니라면
                remainTime -= 1000 //1초씩 줄이기
        }
    }

    private fun calculateToday(): String {
        val nowTime = System.currentTimeMillis();
        val date = Date(nowTime)
        var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(date)
    }

    private fun getRemainTime(orderTime: String): Long {
        var orderYear = Integer.parseInt(orderTime.substring(0, 4))
        var orderMonth = Integer.parseInt(orderTime.substring(5, 7))
        var orderDay = Integer.parseInt(orderTime.substring(8, 10))
        var orderHours = Integer.parseInt(orderTime.substring(11, 13))
        var orderMinutes = Integer.parseInt(orderTime.substring(14, 16))
        var orderSec = 0

        var currentTime = calculateToday()
        var todayYear = Integer.parseInt(currentTime.substring(0, 4))
        var todayMonth = Integer.parseInt(currentTime.substring(5, 7))
        var todayDay = Integer.parseInt(currentTime.substring(8, 10))
        var todayHours = Integer.parseInt(currentTime.substring(11, 13))
        var todayMinutes = Integer.parseInt(currentTime.substring(14, 16))
        var todaySec = Integer.parseInt(currentTime.substring(17, 19))

        var today = Calendar.getInstance().apply {
            set(Calendar.YEAR, todayYear)
            set(Calendar.MONTH, todayMonth)
            set(Calendar.DAY_OF_MONTH, todayDay)
        }.timeInMillis + (60000 * 60 * todayHours) + (60000 * todayMinutes) + (1000* todaySec)

        var order = Calendar.getInstance().apply {
            set(Calendar.YEAR, orderYear)
            set(Calendar.MONTH, orderMonth)
            set(Calendar.DAY_OF_MONTH, orderDay)
        }.timeInMillis + (60000 * 60 * orderHours) + (60000 * orderMinutes) + (1000* 0)

        var remainTime = order - today //남은 시간 밀리세컨드 단위

        return remainTime
    }

    override fun partyJoinAPISuccess() {
        // 파이어 베이스 채팅방 사용자 닉네임 추가
        joinPartyChattingRoom()

        belongStatus = "Y"
        binding.lookPartyRequestTv.text = "채팅방 가기"
        binding.lookMatchingNumber.text = (partyData.currentMatching+1).toString() + "/" + partyData.maxMatching // 현재 매칭 수 + 1

        val dialogPartyRequestComplete = DialogPartyRequestComplete()
        dialogPartyRequestComplete.dialogPartyRequestCompleteView = this
        dialogPartyRequestComplete.show(parentFragmentManager, "partyRequestComplete")

        val handler = Handler()
        handler.postDelayed({
            dialogPartyRequestComplete.dismiss()
        }, 3000)

        dialogPartyRequest!!.dismiss()
    }

    override fun partyJoinAPIFail() {
        showToast("이미 매칭 신청이 완료된 배달 파티입니다.")
    }

    // 채팅방으로 이동
    override fun showPartyChattingRoom() {
        val chatUUID = partyData.uuid
        db.collection("Rooms").document(chatUUID).get()
            .addOnSuccessListener {doc ->
                if (doc != null){
                    val value = doc.data!!["roomInfo"] as HashMap<String, Any>
                    val chatName = value["title"].toString()
                    val intent = Intent(activity, ChattingRoomActivity::class.java)
                    intent.putExtra("roomName", chatName)
                    intent.putExtra("roomUuid", chatUUID)
                    startActivity(intent)
                }
            }
            .addOnFailureListener { e -> Log.w("firebase", "Error update document", e) }
    }

    // 파이어 베이스 participants 추가
    fun joinPartyChattingRoom(){
        db.collection("Rooms")
            .document(partyData.uuid)
            .update("roomInfo.participants", FieldValue.arrayUnion(ParticipantsInfo(calculateToday(), getNickname().toString()))) //현재시간, 닉네임
            .addOnSuccessListener {
                // 00 님이 입장했습니다 시스템 메시지 추가 부분
                val uuid = UUID.randomUUID().toString()
                var time = calculateDate()
                var data = hashMapOf(
                    "content" to "${getNickname()}님이 입장하셨습니다",
                    "nickname" to getNickname(),
                    "isSystemMessage" to true,
                    "time" to time,
                    "userImgUrl" to "이미지 링크"
                )
                db.collection("Rooms").document(partyData.uuid).collection("Messages")
                    .document(uuid).set(data).addOnSuccessListener { }
            }
            .addOnFailureListener { e -> Log.w("firebase", "Error update document", e) }

    }

    private fun calculateDate(): String {
        val now: Long = System.currentTimeMillis()
        val simpleDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa")
        var date: String = simpleDate.format(Date(now)).toString()
        Log.d("ampm", date.toString())
        if (date.substring(20) == "오전" && date.substring(11, 13) == "12")
            date = date.substring(0, 11) + "00" + date.substring(13, 20)
        else if (date.substring(20) == "오후" && date.substring(11, 13) == "12")
            date = date.substring(0, 11) + (Integer.parseInt(date.substring(11, 13))).toString() + date.substring(13, 20)
        else date = date.substring(0, 11) + (Integer.parseInt(date.substring(11, 13)) + 12).toString() + date.substring(13, 20)
        return date
    }

    private fun loadingStart() {
        loadingAnimationView = binding.animationView
        binding.animationViewLayout.visibility = View.VISIBLE
        loadingAnimationView.visibility = View.VISIBLE
        loadingAnimationView.playAnimation()
        loadingAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
            }
            override fun onAnimationEnd(animation: Animator?) {
                // initAfterBinding()
            }
            override fun onAnimationCancel(p0: Animator?) {
            }
            override fun onAnimationRepeat(p0: Animator?) {
            }
        })
    }

    private fun loadingStop() {
        loadingAnimationView.cancelAnimation()
        binding.animationViewLayout.visibility = View.GONE
        loadingAnimationView.visibility = View.GONE
    }
}