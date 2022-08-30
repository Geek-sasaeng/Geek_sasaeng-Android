package com.example.geeksasaeng.Home.Party.UpdateParty

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.airbnb.lottie.model.Marker
import com.example.geeksasaeng.Home.CreateParty.CreatePartyViewModel
import com.example.geeksasaeng.Home.CreateParty.DialogLink
import com.example.geeksasaeng.Home.Party.CreateParty.DialogLocation
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ActivityCreatePartyBinding
import com.example.geeksasaeng.databinding.DialogLocationLayoutBinding
import com.example.geeksasaeng.databinding.DialogLocationLayoutUpdateBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapPoint.GeoCoordinate
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView
import net.daum.mf.map.gen.KakaoMapLibraryAndroidMeta
import java.nio.file.Files.find
import java.util.*

class DialogLocationUpdate: DialogFragment(),
    MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener {

    lateinit var binding: DialogLocationLayoutUpdateBinding
    private var dialogLocationUpdateClickListener: DialogLocationUpdateClickListener? =null
    var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private val GPS_ENABLE_REQUEST_CODE = 2001
    private val PERMISSIONS_REQUEST_CODE = 100
    lateinit var geocoder : Geocoder
    lateinit var mapView : MapView
    lateinit var mapPoint: MapPoint // TODO: 디폴트 맵포인트로 설정
    var locFlag = false // 정보 전달 유무 플래그값
    lateinit var marker : MapPOIItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogLocationLayoutUpdateBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        binding.dialogLocationUpdateLocTv.isSelected = true // 이걸 해줘야 물 흐르는 애니메이션이 가능
        ininKakaoMap()
        initData() //꼭 카카오맵 뒤에 있어야함
        initClickListener()
        return binding.root
    }

    //frag->Activity 정보전달용 코드 시작
    interface DialogLocationUpdateClickListener{
        fun onLocationClicked(mapPoint: MapPoint, locFlag: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogLocationUpdateClickListener =  requireParentFragment() as DialogLocationUpdateClickListener
    }

    override fun onDetach() {
        super.onDetach()
        dialogLocationUpdateClickListener?.onLocationClicked(mapPoint, locFlag)
        binding.dialogLocationUpdateKakaoMapView.removeView(mapView) // 다이얼로그 나가기전에 맵 없애주기
        dialogLocationUpdateClickListener = null
    }
    //frag->Activity 정보전달용 코드 끝

    private fun initData(){
        val latitude = requireArguments().getDouble("latitude")
        val longitude = requireArguments().getDouble("longitude")
        this.mapPoint = MapPoint.mapPointWithGeoCoord(latitude,longitude)
        Log.d("updateLoc", mapPoint.toString())
        drawMap(mapPoint)
        setAddress(mapPoint)
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }


    private fun ininKakaoMap(){

        //현위치 트래킹
/*      mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading)
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }*/
        geocoder = Geocoder(requireContext()) //지오코더 객체 획득
    }

    //위도 경도로 주소 구하는 Reverse-GeoCoding
    private fun setAddress(position: MapPoint) { // tv에 text설정까지 이 함수에서 해준다.
        val geoCoder = Geocoder(context, Locale.KOREA)
        var addr = "주소 오류"

        try {
            addr = geoCoder.getFromLocation(position.mapPointGeoCoord.latitude, position.mapPointGeoCoord.longitude, 1).first().getAddressLine(0)
            binding.dialogLocationUpdateLocTv.text = addr
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //https://bumjae.tistory.com/54 참조

    private fun showDialogForLocationServiceSetting() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            """
            앱을 사용하기 위해서는 위치 서비스가 필요합니다.
            위치 설정을 수정하시겠습니까?
            """.trimIndent()
        )
        builder.setCancelable(true)
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        })
        builder.setNegativeButton("취소",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        builder.create().show()
    }

    fun checkLocationServicesStatus(): Boolean {
        val locationManager : LocationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    override fun onCurrentLocationUpdate(mapView: MapView?, currentLocation: MapPoint?, accuracyInMeters: Float) {
        val mapPointGeo: GeoCoordinate = currentLocation!!.getMapPointGeoCoord()
        //지도 중심점 변경
        mapView!!.setMapCenterPoint(MapPoint.mapPointWithCONGCoord(mapPointGeo.latitude,mapPointGeo.longitude), true)
        Log.i(
            "currentLocation_log",
            java.lang.String.format(
                "MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
                mapPointGeo.latitude,
                mapPointGeo.longitude,
                accuracyInMeters
            )
        )
    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {

    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {

    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {

    }

    fun checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    REQUIRED_PERMISSIONS.get(0)
                )
            ) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(requireContext(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG)
                    .show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    context as Activity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    context as Activity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    private fun initClickListener(){
        binding.dialogLocationUpdateBtn.setOnClickListener {
            locFlag = true // 정상적인 종료임을 표시
            //자기는 종료
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        //엔터치면 검색되게 하는 기능
        binding.dialogLocationUpdateSearchEt.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                binding.dialogLocationUpdateSearchBtn.performClick()
                true
            }else{
                false
            }
        }

        //서치버튼
        binding.dialogLocationUpdateSearchBtn.setOnClickListener {
            //TODO: 서치시 이전 핀 없애주기
            mapView.removePOIItem(marker) // 원래있던 마커 없애주기
            var adr = binding.dialogLocationUpdateSearchEt.text.toString() // 주소 얻어오기
            var list = geocoder.getFromLocationName(adr, 10)

            if (list != null) {
                if (list.size == 0) {
                    binding.dialogLocationUpdateLocTv.setText("올바른 주소를 입력해주세요. ")
                } else {
                    val address: Address = list[0]
                    val lat: Double = address.getLatitude() //위도
                    val lon: Double = address.getLongitude() //경도
                    Log.d("adr", lat.toString()+"/"+lon.toString())
                    this.mapPoint = MapPoint.mapPointWithGeoCoord(lat,lon) // 서치하면 맵포인트 설정됨 (mapPoint 이때 정의됨)
                    //마커생성
                    marker = MapPOIItem()
                    marker.itemName = "요기?"
                    marker.mapPoint = mapPoint
                    marker.markerType = MapPOIItem.MarkerType.BluePin
                    marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                    marker.isDraggable = true //드래그 가능하게 만들기

                    mapView.addPOIItem(marker)
                    mapView!!.setMapCenterPoint(mapPoint, true)//지도 중심점 변경

                    //Reverse Geo-Coding
                    setAddress(this.mapPoint)

                }
            }
        }
    }

    override fun onMapViewInitialized(p0: MapView?) {

    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {

    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {

    }


    // 마커 아이템 관련 이벤트 메소드
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {

    }

    //마커이동시 호출됨
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        //!!
        Log.d("map", "marker이동됨")
        mapView!!.setMapCenterPoint(p2, true)//지도 중심점 변경
        this.mapPoint = p2!! // 맵포인트 수정
        //역지오코딩
        setAddress(p2)

    }


    private fun drawMap(mapPoint: MapPoint){
        //맵  띄우기
        mapView = MapView(activity)
        binding.dialogLocationUpdateKakaoMapView.addView(mapView)
        //마커생성
        marker = MapPOIItem()
        marker.itemName = "요기?"
        marker.mapPoint = mapPoint
        marker.isDraggable = true //드래그 가능하게 만들기
        mapView.addPOIItem(marker)
        mapView!!.setMapCenterPoint(mapPoint, true)//지도 중심점 변경
        mapView.setMapViewEventListener(this)
        mapView.setPOIItemEventListener(this)
    }
}