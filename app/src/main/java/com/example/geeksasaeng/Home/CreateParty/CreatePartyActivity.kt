package com.example.geeksasaeng.Home.CreateParty

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityCreatePartyBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class CreatePartyActivity : BaseActivity<ActivityCreatePartyBinding>(ActivityCreatePartyBinding::inflate), DialogDt.DialogDtNextClickListener, DialogNum.DialogNumNextClickListener, DialogCategory.DialogCategoryNextClickListener, DialogLink.DialogLinkNextClickListener, DialogLocation.DialogLocationNextClickListener {

    lateinit var mapView : MapView

    override fun onResume() {
        Log.d("map", "onResume")
        super.onResume()
        initKakaoMap()
    }

    override fun initAfterBinding() {
        binding.createPartyNumber2Tv.isEnabled = false
        binding.createPartyCategory2Tv.isEnabled = false
        binding.createPartyLink2Tv.isEnabled = false
        binding.createPartyLocation2Tv.isEnabled = false
        initClickListener()
    }

    private fun initKakaoMap(){
        mapView = MapView(this)
        binding.createPartyKakaoMapLocation.addView(mapView)
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

        //주문예정시간 tv 클릭시 (파란색)
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
    override fun onDtClicked(dt: String) {
        //사용자가 선택한 날짜 표시
        binding.createPartyDate2Tv.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.createPartyDate2Tv.text = dt

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

    override fun onLinkClicked(link: String) {
        //사용자가 선택한 식당 링크 표시
        binding.createPartyLink2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyLink2Tv.text = link
        binding.createPartyKakaoMapLocation.removeView(mapView) // 이제 link에서 다음을 클릭했다는 건 DialogLocation에서 지도 띄워야하니까 파티 생성하기 맵뷰는 삭제해주기
    }

    override fun onLocationClicked(loc: String, mapPoint: MapPoint) {
        //사용자가 선택한 위치 표시
        binding.createPartyLocation2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyLocation2Tv.text = loc
        //맵 다시 띄우기
        mapView = MapView(this)
        binding.createPartyKakaoMapLocation.addView(mapView)
        //마커생성
        val marker = MapPOIItem()
        marker.itemName = "요기?"
        marker.mapPoint = mapPoint
        mapView.addPOIItem(marker)
        mapView!!.setMapCenterPoint(mapPoint, true)//지도 중심점 변경
    }
}