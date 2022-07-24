package com.example.geeksasaeng.Home.CreateParty

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.geeksasaeng.Home.CreateParty.Retrofit.CreatePartyDefaultLocResult
import com.example.geeksasaeng.Home.CreateParty.Retrofit.CreatePartyDefaultLocView
import com.example.geeksasaeng.Home.CreateParty.Retrofit.CreatePartyService
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Utils.getJwt
import com.example.geeksasaeng.databinding.ActivityCreatePartyBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.util.*

class CreatePartyActivity : BaseActivity<ActivityCreatePartyBinding>(ActivityCreatePartyBinding::inflate), CreatePartyDefaultLocView,
    DialogDt.DialogDtNextClickListener, DialogNum.DialogNumNextClickListener, DialogCategory.DialogCategoryNextClickListener, DialogLink.DialogLinkNextClickListener, DialogLocation.DialogLocationNextClickListener {

    lateinit var mapView : MapView
    lateinit var mapPoint: MapPoint
    var isNotFirstClick : Boolean = false // 첫 클릭
    lateinit var time: String //TODO: 얘네들 PARSING해서 얻을 수 있어도 좋을 듯
    lateinit var date: String

    private lateinit var createPartyService: CreatePartyService

    override fun initAfterBinding() {
        binding.createPartyNumber2Tv.isEnabled = false
        binding.createPartyCategory2Tv.isEnabled = false
        binding.createPartyLink2Tv.isEnabled = false
        binding.createPartyLocation2Tv.isEnabled = false
        initClickListener()
        initKakaoMap()
    }

    private fun initKakaoMap(){
        Log.d("kakaodefault", "onstart")
        createPartyService = CreatePartyService() //서비스 객체 생성
        createPartyService.setCreatePartyDefaultLocView(this)
        createPartyService.getDefaultLoc(1) //★ default 기숙사 위치 불러오는 함수 호출
    }

    private fun initClickListener(){
        //같이 먹고 싶어요 체크버튼 클릭시
        binding.createPartyTogetherCheckBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.createPartyTogetherTv.setTextColor(ContextCompat.getColor(this, R.color.main))
            } else {
                binding.createPartyTogetherTv.setTextColor(ContextCompat.getColor(this,R.color.gray_2))
            }
        }

        binding.createPartyTogetherTv.setOnClickListener {
            //체크되어있었으면 해제, 안체크 되어있었으면 체크 시키기
            binding.createPartyTogetherCheckBtn.isChecked = !binding.createPartyTogetherCheckBtn.isChecked
        }

        //<여기서부터 배달 생성에 필요한 정보 입력 부분>
        //주문예정시간 tv 클릭시 (파란색)
        binding.createPartyDate2ColoredTv.setOnClickListener {
            // activity에서 커스텀 다이얼로그 띄우는 코드
            DialogDt().show(supportFragmentManager, "CustomDialog")
            binding.createPartyNumber2Tv.isEnabled = true
            binding.createPartyCategory2Tv.isEnabled = true
            binding.createPartyLink2Tv.isEnabled =true
            binding.createPartyLocation2Tv.isEnabled = true
        }

        //주문예정시간 tv 클릭시
        binding.createPartyDate2Tv.setOnClickListener {
            // activity에서 커스텀 다이얼로그 띄우는 코드
            DialogDt().show(supportFragmentManager, "CustomDialog")
        }

        //매칭 인원 tv
        binding.createPartyNumber2Tv.setOnClickListener {
            DialogNum().show(supportFragmentManager, "CustomDialog")
        }

        //카테고리 tv
        binding.createPartyCategory2Tv.setOnClickListener {
            DialogCategory().show(supportFragmentManager, "CustomDialog")
        }

        //링크 tv
        binding.createPartyLink2Tv.setOnClickListener {
            DialogLink().show(supportFragmentManager, "CustomDialog")
        }

        //수령장소 tv
        binding.createPartyLocation2Tv.setOnClickListener {
            binding.createPartyKakaoMapLocation.removeView(mapView) // 맵 사용해야하니까 지우기
            DialogLocation().show(supportFragmentManager, "CustomDialog")
        }

    }

    //다이얼로그에서 next버튼 클릭시 값 받아오기
    override fun onDtClicked(date: String, time: String) {
        //사용자가 선택한 날짜 표시
        binding.createPartyDate2Tv.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.createPartyDate2Tv.text = date+ time

        //파란색 버튼 없애고 회색버튼으로 띄우기
        binding.createPartyDate2Tv.visibility = View.VISIBLE
        binding.createPartyDate2ColoredTv.visibility = View.INVISIBLE
    }

    override fun onNumClicked(num: String) {
        //사용자가 선택한 인원수 표시
        binding.createPartyNumber2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyNumber2Tv.text = num
    }

    override fun onCategoryClicked(category: String) {
        //사용자가 선택한 카테고리 표시
        binding.createPartyCategory2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyCategory2Tv.text = category
    }

    override fun onLinkClicked(link: String, flagNext: Boolean) {
        //사용자가 선택한 식당 링크 표시
        binding.createPartyLink2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyLink2Tv.text = link
        if(flagNext){ //링크 다이얼로그 => 위치 다이얼로그로 넘어간 경우에만 맵 삭제
            binding.createPartyKakaoMapLocation.removeView(mapView) // 이제 link에서 다음을 클릭했다는 건 DialogLocation에서 지도 띄워야하니까 파티 생성하기 맵뷰는 삭제해주기
        }
    }

    override fun onLocationClicked(loc: String, mapPoint: MapPoint, locFlag: Boolean) {
        //사용자가 선택한 위치 표시
        binding.createPartyLocation2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        if(locFlag){ // 정보를 바꾸고 싶을때만 바꾸기 위함
            binding.createPartyLocation2Tv.text = loc
            this.mapPoint = mapPoint
        }
        drawMap(mapPoint)
    }

    private fun drawMap(mapPoint: MapPoint){
        //맵 다시 띄우기
        mapView = MapView(this)
        binding.createPartyKakaoMapLocation.addView(mapView)
        //마커생성
        val marker = MapPOIItem()
        marker.itemName = "요기?"
        marker.isShowCalloutBalloonOnTouch = false // 클릭시 말풍선을 보여줄지 여부 (false)
        marker.mapPoint = mapPoint
        mapView.addPOIItem(marker)
        mapView!!.setMapCenterPoint(mapPoint, true)//지도 중심점 변경
    }

    override fun onDefaultLocSuccess(result: CreatePartyDefaultLocResult) {
        Log.d("kakaodefault", "성공함")
        Log.d("kakaodefault", result.latitude.toString()+"/"+result.longitude.toString())
        //위도 경도로 MapPoint만들기
        this.mapPoint = MapPoint.mapPointWithGeoCoord(result.latitude,result.longitude) // default로 맵포인트 설정됨
        drawMap(mapPoint) // default 위치 정보 맵 위에 표시
        getAddress(mapPoint) // default 위치 정보 tv에 표시
    }

    override fun onDefaultLocFailure() {
        Log.d("kakaodefault", "defaultLoc 불러오기 실패함")
    }

    //위도 경도로 주소 구하는 Reverse-GeoCoding
    private fun getAddress(position: MapPoint) {
        val geoCoder = Geocoder(this, Locale.KOREA)
        var addr = "주소 오류"

        try {
            addr = geoCoder.getFromLocation(position.mapPointGeoCoord.latitude, position.mapPointGeoCoord.longitude, 1).first().getAddressLine(0)
            binding.createPartyLocation2Tv.text = addr
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}