package com.example.geeksasaeng.Home.Party.UpdateParty

import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.geeksasaeng.Home.Party.UpdateParty.Retrofit.UpdatePartyRequest
import com.example.geeksasaeng.Home.Party.UpdateParty.Retrofit.UpdatePartyService
import com.example.geeksasaeng.Home.Party.UpdateParty.Retrofit.UpdatePartyView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyUpdateBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.text.SimpleDateFormat
import java.util.*

//TODO: 얘이름 UpdateParty로 고치고 싶당
class PartyUpdateFragment: BaseFragment<FragmentDeliveryPartyUpdateBinding>(FragmentDeliveryPartyUpdateBinding::inflate), UpdatePartyView,
    DialogDtUpdate.DialogDtUpdateClickListener, DialogNumUpdate.DialogNumUpdateClickListener, DialogCategoryUpdate.DialogCategoryUpdateClickListener,
    DialogLinkUpdate.DialogLinkUpdateClickListener, DialogLocationUpdate.DialogLocationUpdateClickListener{

    lateinit var mapView : MapView
    lateinit var mapPoint: MapPoint

    private lateinit var updatePartyService: UpdatePartyService

    var authorStatus: Boolean? = null
    var chief: String? = null
    var chiefProfileImgUrl: String? = null
    var content: String? = null
    var currentMatching: Int = 0
    var dormitory: Int = 0
    var foodCategory: String? = null
    var hashTag: Boolean? = null
    var partyId: Int = 0
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var matchingStatus: String? = null
    var maxMatching: Int = 0
    var orderTime: String? = null
    var storeUrl: String? = null
    var title: String? = null
    var updatedAt: String? = null
    var isFirst: Boolean = true // 파티 생성하기 화면이 만들어진게 최초인지 확인 위함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        updatePartyService = UpdatePartyService() // 서비스 객체 생성
        updatePartyService.setUpdatePartyView(this)
    }

    override fun initAfterBinding() {
        initGetData()
        initTextWatcher()
        initClickListener()
    }

    private fun initGetData(){ // 파티 보기에서 파티 수정하기로 넘어오면서 데이터 받아오기
        if(this.isFirst){
            authorStatus = requireArguments().getBoolean("authorStatus")
            chief = requireArguments().getString("chief")
            chiefProfileImgUrl = requireArguments().getString("chiefProfileImgUrl")
            content = requireArguments().getString("content")
            currentMatching = requireArguments().getInt("currentMatching")
            dormitory = requireArguments().getInt("dormitory")
            foodCategory = requireArguments().getString("foodCategory") // 카테고리
            hashTag = requireArguments().getBoolean("hashTag")
            partyId = requireArguments().getInt("partyId")
            latitude = requireArguments().getDouble("latitude", latitude)
            longitude = requireArguments().getDouble("longitude", longitude)
            matchingStatus = requireArguments().getString("matchingStatus")
            maxMatching = requireArguments().getInt("maxMatching") // 최대매칭인원
            orderTime = requireArguments().getString("orderTime") // 주문 예정시간
            storeUrl = requireArguments().getString("storeUrl")
            title = requireArguments().getString("title")
            updatedAt = requireArguments().getString("updatedAt")
            Log.d("partyUpdate", latitude.toString()+"/"+longitude.toString())
            Log.d("partyUpdate", orderTime.toString())

            binding.deliveryPartyUpdateTogetherCheckBtn.isChecked = hashTag as Boolean // 같이 먹어요 해시태그
            binding.deliveryPartyUpdateTitleEt.setText(title) //제목
            binding.deliveryPartyUpdateContentEt.setText(content) // 콘텐츠
            binding.deliveryPartyUpdateDate2Tv.text = "${orderTime!!.substring(5, 7)}월 ${orderTime!!.substring(8, 10)}일" +" "+ "${orderTime!!.substring(11, 13)}시 ${orderTime!!.substring(14, 16)}분"
            binding.deliveryPartyUpdateNumber2Tv.text = maxMatching.toString() +"명" //매칭인원선택
            binding.deliveryPartyUpdateCategory2Tv.text = foodCategory //카테고리 선택
            binding.deliveryPartyUpdateLink2Tv.text = storeUrl // 식당 링크
            binding.deliveryPartyUpdateLocation2Tv.text = getAddress(latitude, longitude) // 수령장소
            // 카카오맵에 띄우기
            this.mapPoint = MapPoint.mapPointWithGeoCoord(latitude,longitude)
            drawMap(mapPoint)
            this.isFirst = false
        }
    }

    private fun initTextWatcher(){
        binding.deliveryPartyUpdateTitleEt.addTextChangedListener ( object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                title = binding.deliveryPartyUpdateTitleEt.text.toString()
                checking()
            }

        } )

        binding.deliveryPartyUpdateContentEt.addTextChangedListener ( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                content = binding.deliveryPartyUpdateContentEt.text.toString()
                checking()
            }
        })
    }

    private fun checking(){
        Log.d("cherry", ( hashTag.toString() != requireArguments().getBoolean("hashTag").toString()||
                content.toString() != requireArguments().getString("content").toString()||
                title.toString() != requireArguments().getString("title").toString()||
                orderTime.toString() != requireArguments().getString("orderTime").toString() ||
                maxMatching.toString() != requireArguments().getInt("maxMatching").toString() ||
                foodCategory.toString() != requireArguments().getString("foodCategory").toString() ||
                storeUrl.toString() != requireArguments().getString("storeUrl").toString() ||
                latitude.toString() != requireArguments().getDouble("latitude", latitude).toString() ||
                longitude.toString() != requireArguments().getDouble("longitude", longitude).toString()
                ).toString())

        if( (binding.deliveryPartyUpdateTitleEt.text.length in 1..20 &&
            binding.deliveryPartyUpdateContentEt.text.length in 1..500) &&
            (hashTag.toString() != requireArguments().getBoolean("hashTag").toString()||
            content.toString() != requireArguments().getString("content").toString()||
            title.toString() != requireArguments().getString("title").toString()||
            orderTime.toString() != requireArguments().getString("orderTime").toString() ||
            maxMatching.toString() != requireArguments().getInt("maxMatching").toString() ||
            foodCategory.toString() != requireArguments().getString("foodCategory").toString() ||
            storeUrl.toString() != requireArguments().getString("storeUrl").toString() ||
            latitude.toString() != requireArguments().getDouble("latitude", latitude).toString() ||
            longitude.toString() != requireArguments().getDouble("longitude", longitude).toString()) && (compareDate(orderTime.toString()))
        ){ // 완료버튼 파란색으로 바꿔주기 (조건: 제목, 콘텐츠가 글자수 내에 들어있고, 설정된 시간이 현재시간보다 미래일때 변한게 하나라도 있으면 수정가능하게)
            binding.deliveryPartyUpdateCompleteBtnTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
            if(!binding.deliveryPartyUpdateCompleteBtnTv.isEnabled){
                binding.deliveryPartyUpdateCompleteBtnTv.isEnabled = true
            }
        }else{
            binding.deliveryPartyUpdateCompleteBtnTv.setTextColor((Color.parseColor("#BABABA")))
            if(binding.deliveryPartyUpdateCompleteBtnTv.isEnabled){ // 이렇게 조건문으로 안 감싸도 될것 같은데 안 그럼 오류가 나더라고,,?
                binding.deliveryPartyUpdateCompleteBtnTv.isEnabled = false
            }
        }
    }

    private fun compareDate(time: String): Boolean{ //현재보다 미래인지 체크 위함
        var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date1 = sdf.parse(time)
        val currentTime = Calendar.getInstance().time
        return date1.after(currentTime)
    }

    private fun initClickListener(){

        //백버튼
        binding.deliveryPartyUpdateBackBtnIv.setOnClickListener {
            //TODO: 안돼...
            Log.d("cherry", "파티 수정하기 백버튼 눌러짐")
            //파티 수정=> 파티 보기로 다시 이동
            //종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
            activity?.supportFragmentManager?.popBackStack()
        }

        //완료버튼
        binding.deliveryPartyUpdateCompleteBtnTv.setOnClickListener {
            Log.d("cherry", "버튼 눌려짐")
            var numCategory : Int = 0
            //foodcategory(String)에 따른 아이디값(int)변환
            when(foodCategory){
                "한식"-> numCategory = 1
                "중식"-> numCategory = 3
                "분식"-> numCategory = 5
                "회/돈까스"-> numCategory =7
                "디저트/음료"-> numCategory = 9
                "양식"-> numCategory = 2
                "일식"-> numCategory = 4
                "치킨/피자"-> numCategory = 6
                "패스트 푸드"-> numCategory = 8
                "기타"-> numCategory = 10
                else->{}
            }
            val updatePartyResult = UpdatePartyRequest(content.toString(), numCategory ,hashTag,latitude,longitude, maxMatching, orderTime, storeUrl, title)
            updatePartyService.updatePartySender(dormitory,partyId,updatePartyResult)

        }


        binding.deliveryPartyUpdateTogetherCheckBtn.setOnCheckedChangeListener { //같이 먹고 싶어요 체크버튼 클릭시
                buttonView, isChecked ->
            if (isChecked) {
                binding.deliveryPartyUpdateTogetherTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
            } else {
                binding.deliveryPartyUpdateTogetherTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_2))
            }
        }

        binding.deliveryPartyUpdateTogetherTv.setOnClickListener {
            //체크되어있었으면 해제, 안체크 되어있었으면 체크 시키기
            binding.deliveryPartyUpdateTogetherCheckBtn.isChecked = !binding.deliveryPartyUpdateTogetherCheckBtn.isChecked
            hashTag = binding.deliveryPartyUpdateTogetherCheckBtn.isChecked
        }

        // 여기서 부터 dialog 띄우는 부분
        binding.deliveryPartyUpdateDate2Tv.setOnClickListener {
            val dialogDtUpdate =  DialogDtUpdate()
            var bundle = Bundle()
            bundle.putString("DT", orderTime) // 주문시간 넘겨줌
            dialogDtUpdate.arguments = bundle // 번들 넘겨주기
            dialogDtUpdate.show(childFragmentManager, "CustomDialog")
        }

        binding.deliveryPartyUpdateNumber2Tv.setOnClickListener {
            val dialogNumUpdate = DialogNumUpdate()
            var bundle = Bundle()
            bundle.putString("Num", maxMatching.toString()) // 최대 매칭인원 넘겨줌
            dialogNumUpdate.arguments = bundle // 번들 넘겨주기
            dialogNumUpdate.show(childFragmentManager, "CustomDialog")
        }

        binding.deliveryPartyUpdateCategory2Tv.setOnClickListener {
            val dialogCategoryUpdate =  DialogCategoryUpdate()
            var bundle = Bundle()
            bundle.putString("Category", foodCategory) // 카테고리 넘겨줌
            dialogCategoryUpdate.arguments = bundle // 번들 넘겨주기
            dialogCategoryUpdate.show(childFragmentManager, "CustomDialog")
        }

        binding.deliveryPartyUpdateLink2Tv.setOnClickListener {
            val dialogLinkUpdate =  DialogLinkUpdate()
            var bundle = Bundle()
            bundle.putString("Link", storeUrl) // 식당 링크 넘겨줌
            dialogLinkUpdate.arguments = bundle // 번들 넘겨주기
            dialogLinkUpdate.show(childFragmentManager, "CustomDialog")
        }

        binding.deliveryPartyUpdateLocation2Tv.setOnClickListener {
            val dialogLocationUpdate =  DialogLocationUpdate()
            var bundle = Bundle()
            bundle.putDouble("latitude", latitude) // 주소 넘겨줌
            bundle.putDouble("longitude", longitude) // 주소 넘겨줌
            dialogLocationUpdate.arguments = bundle // 번들 넘겨주기
            binding.deliveryPartyUpdateKakaoMapLocation.removeView(mapView) // 맵 사용해야하니까 지우기
            dialogLocationUpdate.show(childFragmentManager, "CustomDialog")
        }

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

    private fun drawMap(mapPoint: MapPoint){
        //맵 다시 띄우기
        mapView = MapView(requireActivity())
        mapView.setOnTouchListener { v, event ->
            binding.deliveryPartyUpdateSv.requestDisallowInterceptTouchEvent(true) //부모에게 Touch Event를 빼앗기지 않게 할 수 있다.
            return@setOnTouchListener false
        }
        binding.deliveryPartyUpdateKakaoMapLocation.addView(mapView)
        //마커생성
        val marker = MapPOIItem()
        marker.itemName = "요기?"
        marker.isShowCalloutBalloonOnTouch = false // 클릭시 말풍선을 보여줄지 여부 (false)
        marker.mapPoint = mapPoint
        mapView.addPOIItem(marker)
        mapView!!.setMapCenterPoint(mapPoint, true)//지도 중심점 변경
    }

    // 다이얼로그 닫을 때 호출됨
    override fun onDtClicked(dt: String) {
        orderTime = dt
        binding.deliveryPartyUpdateDate2Tv.text = "${orderTime!!.substring(5, 7)}월 ${orderTime!!.substring(8, 10)}일" +" "+ "${orderTime!!.substring(11, 13)}시 ${orderTime!!.substring(14, 16)}분"
        checking()
    }

    override fun onNumClicked(num: Int) {
        maxMatching = num
        binding.deliveryPartyUpdateNumber2Tv.text = maxMatching.toString() +"명" //매칭인원선택
        checking()
    }

    override fun onCategoryClicked(category: String) {
        foodCategory = category
        binding.deliveryPartyUpdateCategory2Tv.text = foodCategory //카테고리 선택
        checking()
    }

    override fun onLinkClicked(link: String) {
        binding.deliveryPartyUpdateLink2Tv.text = link
        if (link!="링크를 입력해주세요"){
            storeUrl = link
        }
        checking()
    }

    override fun onLocationClicked(mapPoint: MapPoint, locFlag:Boolean) {
        if (locFlag) { // 정보 갱신
            latitude = mapPoint.mapPointGeoCoord.latitude
            longitude = mapPoint.mapPointGeoCoord.longitude
        }
        binding.deliveryPartyUpdateLocation2Tv.text = getAddress(latitude, longitude) // 수령장소
        // 카카오맵에 띄우기
        drawMap(MapPoint.mapPointWithGeoCoord(latitude,longitude))
        checking()
    }

    //수정 성공, 실패
    override fun onUpdatePartySuccess() {
        //종료
        activity?.supportFragmentManager?.beginTransaction()
            ?.remove(this)?.commit()
        activity?.supportFragmentManager?.popBackStack()
        CustomToastMsg.createToast((requireContext()), "수정이 완료되었습니다", "#8029ABE2", 58)?.show()
    }

    override fun onUpdatePartyFailure(message: String) {
        Log.d("cherry", message)
    }
}