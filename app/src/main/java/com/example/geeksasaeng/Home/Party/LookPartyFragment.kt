package com.example.geeksasaeng.Home.Party

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDataService
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDetailResult
import com.example.geeksasaeng.Home.Party.Retrofit.PartyDetailView
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentLookPartyBinding

class LookPartyFragment: BaseFragment<FragmentLookPartyBinding>(FragmentLookPartyBinding::inflate), PartyDetailView {

    var deliveryItemId: Int? = null
    var status: String? = null
    var authorStatus: Boolean? = null
    lateinit var partyData: PartyDetailResult

    override fun initAfterBinding() {
        initClickListener()

        binding.lookPartyProgressBar.visibility = View.VISIBLE

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
            dialogFragment.show(parentFragmentManager, dialogTag)
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

        // TODO: 위도, 경도로 수령장소 문자 넣어주기
        binding.lookLocateText.text = "임시 값"
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
    }

    override fun partyDetailFailure(code: Int, message: String) {
        showToast(message)
    }
}