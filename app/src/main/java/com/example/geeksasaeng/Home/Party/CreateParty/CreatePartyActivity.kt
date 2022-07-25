package com.example.geeksasaeng.Home.CreateParty

import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.Home.CreateParty.Retrofit.*
import com.example.geeksasaeng.Home.Party.CreateParty.DialogCategory
import com.example.geeksasaeng.Home.Party.CreateParty.DialogDt
import com.example.geeksasaeng.Home.Party.CreateParty.DialogLocation
import com.example.geeksasaeng.Home.Party.CreateParty.DialogNum
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Utils.getJwt
import com.example.geeksasaeng.databinding.ActivityCreatePartyBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.util.*

//TODO: 여기서는 잘하면 CreatePartyDefaultLocView 이거 없이도 가능할지도? 7.30-31에 이 부분 다시 봐보기
class CreatePartyActivity : BaseActivity<ActivityCreatePartyBinding>(ActivityCreatePartyBinding::inflate), CreatePartyDefaultLocView, CreatePartyView,
    DialogDt.DialogDtNextClickListener, DialogNum.DialogNumNextClickListener, DialogCategory.DialogCategoryNextClickListener, DialogLink.DialogLinkNextClickListener, DialogLocation.DialogLocationNextClickListener {

    lateinit var mapView : MapView
    lateinit var mapPoint: MapPoint

    private lateinit var createPartyService: CreatePartyService
    private lateinit var createPartyVM: CreatePartyViewModel

    override fun initAfterBinding() {
        createPartyVM = ViewModelProvider(this).get(CreatePartyViewModel::class.java)
        binding.createPartyNumber2Tv.isEnabled = false
        binding.createPartyCategory2Tv.isEnabled = false
        binding.createPartyLink2Tv.isEnabled = false
        binding.createPartyLocation2Tv.isEnabled = false
        createPartyService = CreatePartyService() //서비스 객체 생성
        createPartyService.setCreatePartyDefaultLocView(this)
        createPartyService.setCreatePartyView(this)
        initTextWatcher()
        initClickListener()
        initKakaoMap()
    }

    private fun initTextWatcher(){
        binding.createPartyTitleEt.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                Log.d("jjang", "텍스트와쳐-title")
                Log.d("jjang", "textwatcher-title"+ checking().toString())
                if(checking()){ // 등록버튼 파란색으로 바꿔주기
                    binding.createPartyRegisterBtnTv.setTextColor(ContextCompat.getColor(this@CreatePartyActivity, R.color.main))
                }else{
                    binding.createPartyRegisterBtnTv.setTextColor((Color.parseColor("#BABABA")))
                }
            }
        } )

        binding.createPartyContentEt.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                Log.d("jjang", "텍스트와쳐-content")
                Log.d("jjang", "textwatcher-content"+ checking().toString())
                if(checking()){ // 등록버튼 파란색으로 바꿔주기
                    binding.createPartyRegisterBtnTv.setTextColor(ContextCompat.getColor(this@CreatePartyActivity, R.color.main))
                }else{
                    binding.createPartyRegisterBtnTv.setTextColor((Color.parseColor("#BABABA")))
                }
            }
        } )
    }

    private fun initKakaoMap(){
        Log.d("kakaodefault", "onstart")
        createPartyService.getDefaultLoc(1) //★ default 기숙사 위치 불러오는 함수 호출
    }


    private fun checking() : Boolean {
        Log.d("jjang", ((binding.createPartyContentEt.text.toString().length>=1&&binding.createPartyContentEt.text.toString().length<=20)&&
                (binding.createPartyTitleEt.text.toString().length>=1&&binding.createPartyTitleEt.text.toString().length<=500) &&
                createPartyVM.getDate2().toString() != "null" &&
                createPartyVM.getTime2().toString() != "null" &&
                createPartyVM.getMaxMatching().toString() != "null" &&
                createPartyVM.getCategory().toString() != "null" &&
                createPartyVM.getMapPoint().toString() != "null").toString())

        return ((binding.createPartyContentEt.text.toString().length>=1&&binding.createPartyContentEt.text.toString().length<=20)&&
                (binding.createPartyTitleEt.text.toString().length>=1&&binding.createPartyTitleEt.text.toString().length<=500) &&
                createPartyVM.getDate2().toString() != "null" &&
                createPartyVM.getTime2().toString() != "null" &&
                createPartyVM.getMaxMatching().toString() != "null" &&
                createPartyVM.getCategory().toString() != "null" &&
                createPartyVM.getMapPoint().toString() != "null")
    }

    private fun initClickListener(){

        binding.createPartyBackBtnIv.setOnClickListener {
            startActivityWithClear(MainActivity::class.java)
        }

        binding.createPartyRegisterBtnTv.setOnClickListener { //등록버튼 클릭시
            //TODO: "2022-07-26 16:29:30" => 이 시간형식은 어떻게 구할까..

            if(checking()){
                Log.d("jjang", binding.createPartyContentEt.text.toString()+"/"+  createPartyVM.getCategoryInt()!!.toString()+"/"+ binding.createPartyTogetherCheckBtn.isChecked.toString()+"/"+  createPartyVM.getMapPoint()!!.mapPointGeoCoord.latitude.toString() +"/"+  createPartyVM.getMapPoint()!!.mapPointGeoCoord.longitude.toString()+"/"+  createPartyVM.getMaxMatching()!!.toString() +"/"+ createPartyVM.getDate2().toString()+ " " + createPartyVM.getTime2().toString() +"/"+  createPartyVM.getStoreUrl()!!.toString() +"/"+ binding.createPartyTitleEt.text.toString())
                val createPartyRequest = CreatePartyRequest(binding.createPartyContentEt.text.toString(), createPartyVM.getCategoryInt()!!, binding.createPartyTogetherCheckBtn.isChecked, createPartyVM.getMapPoint()!!.mapPointGeoCoord.latitude, createPartyVM.getMapPoint()!!.mapPointGeoCoord.longitude,
                    createPartyVM.getMaxMatching()!!, createPartyVM.getDate2().toString()+ " " + createPartyVM.getTime2().toString(), createPartyVM.getStoreUrl()!!, binding.createPartyTitleEt.text.toString())
                createPartyService.createPartySender(1, createPartyRequest) //★파티 등록하기
                startActivityWithClear(MainActivity::class.java)
            }
        }

        binding.createPartyTogetherCheckBtn.setOnCheckedChangeListener { //같이 먹고 싶어요 체크버튼 클릭시
                buttonView, isChecked ->
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
        if(date==""||time==""){ // 정보가 하나라도 부족하면
            binding.createPartyDate2Tv.text = "주문 예정 시간을 입력해주세요"
        }else{
            binding.createPartyDate2Tv.text = date+ " " + time
        }

        //파란색 버튼 없애고 회색버튼으로 띄우기
        binding.createPartyDate2Tv.visibility = View.VISIBLE
        binding.createPartyDate2ColoredTv.visibility = View.INVISIBLE
        if(checking() == true){ // 등록버튼 파란색으로 바꿔주기
            binding.createPartyRegisterBtnTv.setTextColor(ContextCompat.getColor(this@CreatePartyActivity, R.color.main))
        }else{
            binding.createPartyRegisterBtnTv.setTextColor((Color.parseColor("#BABABA")))
        }
    }

    override fun onNumClicked(num: String) {
        //사용자가 선택한 인원수 표시
        binding.createPartyNumber2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyNumber2Tv.text = num
        if(checking() == true){ // 등록버튼 파란색으로 바꿔주기
            binding.createPartyRegisterBtnTv.setTextColor(ContextCompat.getColor(this@CreatePartyActivity, R.color.main))
        }else{
            binding.createPartyRegisterBtnTv.setTextColor((Color.parseColor("#BABABA")))
        }
    }

    override fun onCategoryClicked(category: String) {
        //사용자가 선택한 카테고리 표시
        binding.createPartyCategory2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyCategory2Tv.text = category
        if(checking() == true){ // 등록버튼 파란색으로 바꿔주기
            binding.createPartyRegisterBtnTv.setTextColor(ContextCompat.getColor(this@CreatePartyActivity, R.color.main))
        }else{
            binding.createPartyRegisterBtnTv.setTextColor((Color.parseColor("#BABABA")))
        }
    }

    override fun onLinkClicked(link: String, flagNext: Boolean) { //flagNext는 카카오 지도 때문에 이용
        //사용자가 선택한 식당 링크 표시
        binding.createPartyLink2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyLink2Tv.text = link
        if(flagNext){ //링크 다이얼로그 => 위치 다이얼로그로 넘어간 경우에만 맵 삭제
            binding.createPartyKakaoMapLocation.removeView(mapView) // 이제 link에서 다음을 클릭했다는 건 DialogLocation에서 지도 띄워야하니까 파티 생성하기 맵뷰는 삭제해주기
        }
        if(checking() == true){ // 등록버튼 파란색으로 바꿔주기
            binding.createPartyRegisterBtnTv.setTextColor(ContextCompat.getColor(this@CreatePartyActivity, R.color.main))
        } else{
            binding.createPartyRegisterBtnTv.setTextColor((Color.parseColor("#BABABA")))
        }
    }

    override fun onLocationClicked(loc: String, mapPoint: MapPoint, locFlag: Boolean) {
        //locFlag가 아마 다이얼로그 바깥을 클릭했는지 , 완료로 탈출했는지 여부를 알려주는 걸꺼야
        //TODO: 내 생각엔 여기선 다이얼로그 바깥 클릭해서 탈출 못하게 하면 안되나?
        //사용자가 선택한 위치 표시
        binding.createPartyLocation2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyLocation2Tv.text = loc
        drawMap(createPartyVM.getMapPoint()!!)
        if(checking() == true){ // 등록버튼 파란색으로 바꿔주기
            binding.createPartyRegisterBtnTv.setTextColor(ContextCompat.getColor(this@CreatePartyActivity, R.color.main))
        } else{
            binding.createPartyRegisterBtnTv.setTextColor((Color.parseColor("#BABABA")))
        }
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

    // 파티 등록하기 성공/실패
    override fun onCreatePartySuccess() {
        Log.d("jjang", "파티 생성 성공(서버로 정보 보냄)")
    }

    override fun onCreatePartyFailure(message: String) {
        Log.d("jjang",message)
    }
}