package com.example.geeksasaeng.Home.CreateParty

import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.Chatting.ChattingRoom.ChattingRoomActivity
import com.example.geeksasaeng.Home.Party.CreateParty.*
import com.example.geeksasaeng.Home.Party.Retrofit.*
import com.example.geeksasaeng.Home.Party.LookParty.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Utils.CustomToastMsg
import com.example.geeksasaeng.Utils.getDormitory
import com.example.geeksasaeng.Utils.getDormitoryId
import com.example.geeksasaeng.databinding.ActivityCreatePartyBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.text.SimpleDateFormat
import java.util.*

//TODO: 여기서는 잘하면 CreatePartyDefaultLocView 이거 없이도 가능할지도? 7.30-31에 이 부분 다시 봐보기
class CreatePartyActivity : BaseActivity<ActivityCreatePartyBinding>(ActivityCreatePartyBinding::inflate), CreatePartyDefaultLocView, CreatePartyView,
    DialogDt.DialogDtNextClickListener, DialogNum.DialogNumNextClickListener, DialogCategory.DialogCategoryNextClickListener, DialogLink.DialogLinkNextClickListener, DialogLocation.DialogLocationNextClickListener,
    DialogPartyName.DialogPartyNameClickListener{

    lateinit var mapView : MapView
    lateinit var mapPoint: MapPoint

    private lateinit var createPartyService: CreatePartyService
    private lateinit var createPartyVM: CreatePartyViewModel

    private var nextable: Boolean = false
    private val db = Firebase.firestore //파이어스토어

    override fun initAfterBinding() {

        createPartyVM = ViewModelProvider(this).get(CreatePartyViewModel::class.java)
        binding.createPartyNumber2Tv.isEnabled = false
        binding.createPartyCategory2Tv.isEnabled = false
        binding.createPartyLink2Tv.isEnabled = false
        binding.createPartyLocation2Tv.isEnabled = false
        createPartyService = CreatePartyService() //서비스 객체 생성
        createPartyService.setCreatePartyDefaultLocView(this)
        createPartyService.setCreatePartyView(this)

        binding.createPartyDate2ColoredTv.text = getCurrentDate()+" "+getCurrentTime()

        getApplicationContext()
        initTextWatcher()
        initClickListener()
        initKakaoMap()
    }

/*    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        val title = binding.createPartyTitleEt.text
        val content = binding.createPartyContentEt.text
        outState.putCharSequence("title", title)
        outState.putCharSequence("content", content)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val title = savedInstanceState.getCharSequence("title")
        val content = savedInstanceState.getCharSequence("content")
        binding.createPartyTitleEt.setText(title)
        binding.createPartyContentEt.setText(content)
    }*/

    private fun initTextWatcher(){
        binding.createPartyTitleEt.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checking()
            }
        } )

        binding.createPartyContentEt.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checking()
            }
        } )
    }

    private fun initKakaoMap(){
        Log.d("kakaodefault", "onstart")
        createPartyService.getDefaultLoc(getDormitoryId()!!) //★ default 기숙사 위치 불러오는 함수 호출
    }


    private fun checking()  {
        //TODO: 왜 등록이 안되는지 정보가 좀 부족한것 같아..!
        Log.d("checking",(binding.createPartyTitleEt.text.length in 1..20).toString()+"/"+(binding.createPartyContentEt.text.length in 1..500).toString()+"/"+(createPartyVM.getDate2().toString() != "null").toString()+"/"+
                (createPartyVM.getTime2().toString() != "null").toString()+"/"+(createPartyVM.getMaxMatching().toString() != "null").toString()+"/"+
                (createPartyVM.getCategory().toString() != "null").toString()+"/"+(createPartyVM.getMapPoint().toString() != "null").toString())
        if ((binding.createPartyTitleEt.text.length in 1..20)&&
                (binding.createPartyContentEt.text.length in 1..500) &&
                createPartyVM.getDate2().toString() != "null" &&
                createPartyVM.getTime2().toString() != "null" &&
                compareDate(createPartyVM.getDate2().toString() +" "+ createPartyVM.getTime2().toString()) &&
                createPartyVM.getMaxMatching().toString() != "null" &&
                createPartyVM.getCategory().toString() != "null" &&
                createPartyVM.getMapPoint().toString() != "null")
        { // 등록조건이 만족되면,
            binding.createPartyRegisterBtnTv.setTextColor(ContextCompat.getColor(this@CreatePartyActivity, R.color.main))
            nextable = true
        }else{
            binding.createPartyRegisterBtnTv.setTextColor((Color.parseColor("#BABABA")))
            nextable= false
        }
    }

    fun getCurrentDate(): String{
        val formatter = SimpleDateFormat("MM월 dd일", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    fun getCurrentTime(): String{
        val formatter = SimpleDateFormat("HH시 mm분", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    private fun compareDate(time: String): Boolean{ //현재보다 미래인지 체크 위함
        var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date1 = sdf.parse(time)
        val currentTime = Calendar.getInstance().time

        return date1.after(currentTime)
    }

    private fun initClickListener(){

        binding.createPartyBackBtnIv.setOnClickListener {
            startActivityWithClear(MainActivity::class.java)
        }

        binding.createPartyRegisterBtnTv.setOnClickListener { //다음버튼
            Log.d("createParty",binding.createPartyRegisterBtnTv.textColors.equals(R.color.main).toString())
            if(nextable){
                DialogAccountNumber().show(supportFragmentManager, "CustomDialog") // 계좌정보 입력 다이얼로그 띄우기
            }else{ // 활성화가 안되어 있는 상태라면

                //사용자에게 뭐가 부족한지 알려주기 위해
                if(!(binding.createPartyTitleEt.text.length in 1..20)){
                    CustomToastMsg.createToast(this, "제목을 입력해주세요", "#8029ABE2", 58)?.show()
                }else if(!(binding.createPartyContentEt.text.length in 1..500)){
                    CustomToastMsg.createToast(this, "내용을 입력해주세요", "#8029ABE2", 58)?.show()
                }else if(createPartyVM.getDate2().toString() == "null" || createPartyVM.getTime2().toString() == "null"){
                    CustomToastMsg.createToast(this, "주문 예정 시간을 입력해주세요", "#8029ABE2", 58)?.show()
                }else if(!compareDate(createPartyVM.getDate2().toString() +" "+ createPartyVM.getTime2().toString())){
                    CustomToastMsg.createToast(this, "현재보다 미래 시간을 입력해주세요", "#8029ABE2", 58)?.show()
                }else if(createPartyVM.getMaxMatching().toString() == "null"){
                    CustomToastMsg.createToast(this, "매칭 인원을 입력해주세요", "#8029ABE2", 58)?.show()
                }else if(createPartyVM.getCategory().toString() == "null"){
                    CustomToastMsg.createToast(this, "카테고리를 입력해주세요", "#8029ABE2", 58)?.show()
                }else if(createPartyVM.getMapPoint().toString() == "null"){
                    CustomToastMsg.createToast(this, "수령장소를 입력해주세요", "#8029ABE2", 58)?.show()
                }
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
    //TODO: 이거 인자값 없애도 될듯 VM쓰면
    override fun onDtClicked() {
        //사용자가 선택한 날짜 표시
        binding.createPartyDate2Tv.setTextColor(ContextCompat.getColor(this, R.color.black))
        if(createPartyVM.getDate()==""||createPartyVM.getTime()==""){ // 정보가 하나라도 부족하면
            binding.createPartyDate2Tv.text = "주문 예정 시간을 입력해주세요"
        }else{
            binding.createPartyDate2Tv.text = createPartyVM.getDate()+ " " + createPartyVM.getTime()
        }

        //파란색 버튼 없애고 회색버튼으로 띄우기
        binding.createPartyDate2Tv.visibility = View.VISIBLE
        binding.createPartyDate2ColoredTv.visibility = View.INVISIBLE
        checking()
    }

    override fun onNumClicked(num: String) {
        //사용자가 선택한 인원수 표시
        binding.createPartyNumber2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyNumber2Tv.text = num
        checking()
    }

    override fun onCategoryClicked(category: String) {
        //사용자가 선택한 카테고리 표시
        binding.createPartyCategory2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyCategory2Tv.text = category
        checking()
    }

    override fun onLinkClicked(link: String, flagNext: Boolean) { //flagNext는 카카오 지도 때문에 이용
        //사용자가 선택한 식당 링크 표시
        binding.createPartyLink2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyLink2Tv.text = link
        if(flagNext){ //링크 다이얼로그 => 위치 다이얼로그로 넘어간 경우에만 맵 삭제
            binding.createPartyKakaoMapLocation.removeView(mapView) // 이제 link에서 다음을 클릭했다는 건 DialogLocation에서 지도 띄워야하니까 파티 생성하기 맵뷰는 삭제해주기
        }
        checking()
    }

    //TODO: locFlag 필요 없네,,,>?? 지우자
    override fun onLocationClicked(loc: String, mapPoint: MapPoint, locFlag: Boolean) {
        //locFlag가 아마 다이얼로그 바깥을 클릭했는지 , 완료로 탈출했는지 여부를 알려주는 걸꺼야
        //TODO: 내 생각엔 여기선 다이얼로그 바깥 클릭해서 탈출 못하게 하면 안되나?
        //사용자가 선택한 위치 표시
        binding.createPartyLocation2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyLocation2Tv.text = loc
        drawMap(createPartyVM.getMapPoint()!!)
        checking()
    }

    private fun drawMap(mapPoint: MapPoint){
        //맵 다시 띄우기
        mapView = MapView(this)
        mapView.setOnTouchListener { v, event ->
            binding.createPartySv.requestDisallowInterceptTouchEvent(true) //부모에게 Touch Event를 빼앗기지 않게 할 수 있다.
            return@setOnTouchListener false
        }
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
    override fun onCreatePartySuccess(result: CreatePartyResult) {
        Log.d("jjang", "파티 생성 성공(서버로 정보 보냄)")
        //TODO:파티보기로 이동
        CustomToastMsg.createToast(this, "파티 생성이 완료되었습니다", "#8029ABE2", 58)?.show()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("status", "lookParty")
        intent.putExtra("deliveryItemId", result.partyId) 
        startActivity(intent)
        finish()

        //firebase에 채팅방 생성하기 위한 데이터 구조 만들기
        var participantsList = ArrayList<String>()
        participantsList.add("방장닉네임") //TODO: 방장닉네임 알아와서 넣어주기

        val roomInfo = hashMapOf(
            "roomInfo" to hashMapOf(
                "accountNumber" to result.accountNumber,
                "bank" to result.bank,
                "category" to "배달파티",
                "isFinish" to false,
                "participants" to participantsList,
                "title" to result.title
            )
        )

        //firebase에 채팅방 생성
        db.collection("Rooms")
            .document(result.uuid)
            .set(roomInfo)
            .addOnSuccessListener { Log.d("firebase", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("firebase", "Error writing document", e) }
    }

    override fun onCreatePartyFailure(message: String) {
        Log.d("jjang",message)
    }

    override fun onCompleteClicked() { //마지막 파티이름 dialog에서 클릭버튼을 누르면
        Log.d("jjang", createPartyVM.getAccountNumber().toString()+"/"+createPartyVM.getAccount().toString()+"/"+createPartyVM.getPartyName().toString()+"/"+binding.createPartyContentEt.text.toString()+"/"+  createPartyVM.getCategoryInt()!!.toString()+"/"+ binding.createPartyTogetherCheckBtn.isChecked.toString()+"/"+  createPartyVM.getMapPoint()!!.mapPointGeoCoord.latitude.toString() +"/"+  createPartyVM.getMapPoint()!!.mapPointGeoCoord.longitude.toString()+"/"+  createPartyVM.getMaxMatching()!!.toString() +"/"+ createPartyVM.getDate2().toString()+ " " + createPartyVM.getTime2().toString() +"/"+  createPartyVM.getStoreUrl()!!.toString() +"/"+ binding.createPartyTitleEt.text.toString())
        val createPartyRequest = CreatePartyRequest(createPartyVM.getAccountNumber().toString(), createPartyVM.getAccount().toString(), createPartyVM.getPartyName().toString(), binding.createPartyContentEt.text.toString(), createPartyVM.getCategoryInt()!!, binding.createPartyTogetherCheckBtn.isChecked, createPartyVM.getMapPoint()!!.mapPointGeoCoord.latitude, createPartyVM.getMapPoint()!!.mapPointGeoCoord.longitude,
            createPartyVM.getMaxMatching()!!, createPartyVM.getDate2().toString()+ " " + createPartyVM.getTime2().toString(), createPartyVM.getStoreUrl()!!, binding.createPartyTitleEt.text.toString())
        createPartyService.createPartySender(getDormitoryId()!!, createPartyRequest) //★파티 등록하기
    }
}