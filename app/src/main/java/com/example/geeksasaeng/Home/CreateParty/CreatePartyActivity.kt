package com.example.geeksasaeng.Home.CreateParty

import android.util.Log
import androidx.core.content.ContextCompat
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityCreatePartyBinding

class CreatePartyActivity : BaseActivity<ActivityCreatePartyBinding>(ActivityCreatePartyBinding::inflate), DialogDt.DialogDtNextClickListener, DialogNum.DialogNumNextClickListener, DialogCategory.DialogCategoryNextClickListener, DialogLocation.DialogLocationNextClickListener {
    override fun initAfterBinding() {
        initClickListener()
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

        //주문예정시간 tv 클릭시
        binding.createPartyDate2Tv.setOnClickListener {
            // activity에서 커스텀 다이얼로그 띄우는 코드
            DialogDt().show(supportFragmentManager, "CustomDialog")
        }

    }

    //다이얼로그에서 next버튼 클릭시 값 받아오기
    override fun onDtClicked(dt: String, orderNow:Boolean) {
        //사용자가 선택한 날짜 표시
        binding.createPartyDate2Tv.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.createPartyDate2Tv.text = dt
        if(orderNow){ //매칭시 바로 주문 여부에 따라서 색 변경해주기
            binding.createPartyImmediateTv.setTextColor(ContextCompat.getColor(this,R.color.main))
        }else{
            binding.createPartyImmediateTv.setTextColor(ContextCompat.getColor(this,R.color.gray_1))
        }

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

    override fun onLocationClicked(loc: String) {
        //사용자가 선택한 위치 표시
        binding.createPartyPlace2Tv.setTextColor(ContextCompat.getColor(this,R.color.black))
        binding.createPartyPlace2Tv.text = loc
    }
}