package com.example.geeksasaeng.Home.Party

import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.method.Touch
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDataService
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDetailResult
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDetailView
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentLookPartyBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer

class LookPartyFragment: BaseFragment<FragmentLookPartyBinding>(FragmentLookPartyBinding::inflate), PartyDetailView, DialogDeliveryOptionMyPopup.PopUpdateClickListener {

    var deliveryItemId: Int? = null
    var status: String? = null
    var authorStatus: Boolean? = null
    lateinit var partyData: PartyDetailResult
    lateinit var mapView : MapView

    private var remainTime : Long = 0
    private var timerTask : Timer? = null

    override fun initAfterBinding() {
        initClickListener()
        binding.lookPartyProgressBar.visibility = View.VISIBLE
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
            (context as MainActivity).supportFragmentManager.beginTransaction().remove(this).commit();
            (context as MainActivity).supportFragmentManager.popBackStack();
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
    }

    private fun initDeliveryPartyData() {
        val partyDetailService = PartyDataService()
        partyDetailService.partyDetailSender(deliveryItemId!!)
        partyDetailService.setPartyDetailView(this)
    }

    override fun partyDetailSuccess(result: PartyDetailResult) {
        partyData = result

        binding.lookPartyWhite.visibility = View.GONE
        binding.lookPartyProgressBar.visibility = View.GONE

        authorStatus = result.authorStatus

        if (result?.chiefProfileImgUrl != null)
            binding.lookHostProfile.setImageURI(Uri.parse(result?.chiefProfileImgUrl))
        else // TODO: 기본 이미지 넣을 예정
            binding.lookHostProfile.setImageResource(R.drawable.ic_default_profile)

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
        Log.d("current", calculateToday().toString())
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
}