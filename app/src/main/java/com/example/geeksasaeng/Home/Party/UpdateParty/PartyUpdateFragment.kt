package com.example.geeksasaeng.Home.Party.UpdateParty

import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.geeksasaeng.Home.Party.CreateParty.DialogDt
import com.example.geeksasaeng.Home.Party.UpdateParty.Retrofit.UpdatePartyView
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentDeliveryPartyUpdateBinding
import java.util.*


class PartyUpdateFragment: BaseFragment<FragmentDeliveryPartyUpdateBinding>(FragmentDeliveryPartyUpdateBinding::inflate), UpdatePartyView {

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

    override fun initAfterBinding() {
        binding.deliveryPartyUpdateLocation2Tv.isSelected = true // 애니메이션 효과 주기 위해서
        initGetData()
        initClickListener()
    }


    private fun initGetData(){ // 파티 보기에서 파티 수정하기로 넘어오면서 데이터 받아오기
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

        binding.deliveryPartyUpdateTogetherCheckBtn.isChecked = hashTag as Boolean // 같이 먹어요 해시태그
        binding.deliveryPartyUpdateTitleEt.setText(title) //제목
        binding.deliveryPartyUpdateContentEt.setText(content) // 콘텐츠
        //주문 예정시간
        binding.deliveryPartyUpdateNumber2Tv.text = maxMatching.toString() +"명" //매칭인원선택
        binding.deliveryPartyUpdateCategory2Tv.text = foodCategory //카테고리 선택
        binding.deliveryPartyUpdateLink2Tv.text = storeUrl // 식당 링크
        binding.deliveryPartyUpdateLocation2Tv.text = getAddress(latitude, longitude) // 수령장소
        // 카카오맵에 띄우기
    }

    private fun initClickListener(){

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
        }

        // 여기서 부터 dialog 띄우는 부분ㅇ
        binding.deliveryPartyUpdateDate2Tv.setOnClickListener {
            DialogDtUpdate().show(requireActivity().supportFragmentManager, "CustomDialog")
        }

        binding.deliveryPartyUpdateNumber2Tv.setOnClickListener {
            DialogNumUpdate().show(requireActivity().supportFragmentManager, "CustomDialog")
        }

        binding.deliveryPartyUpdateCategory2Tv.setOnClickListener {
            DialogCategoryUpdate().show(requireActivity().supportFragmentManager, "CustomDialog")
        }

        binding.deliveryPartyUpdateLink2Tv.setOnClickListener {
            DialogLinkUpdate().show(requireActivity().supportFragmentManager, "CustomDialog")
        }

        binding.deliveryPartyUpdateLocation2Tv.setOnClickListener {
            DialogLocationUpdate().show(requireActivity().supportFragmentManager, "CustomDialog")
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

    override fun onUpdatePartySuccess() {
        //수정 성공하면, 파티보기로 이동하면서 수정이 완료되었습니다 토스트 메세지 띄워줘야해
    }

    override fun onUpdatePartyFailure(message: String) {
        Log.d("updateParty", message)
    }
}