package com.example.geeksasaeng.Home.CreateParty

import android.util.Log
import androidx.core.content.ContextCompat
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.ActivityCreatePartyBinding


class CreatePartyActivity : BaseActivity<ActivityCreatePartyBinding>(ActivityCreatePartyBinding::inflate) {
    override fun initAfterBinding() {
        initClickListener()
    }


    private fun initClickListener(){
        //같이 먹고 싶어요 체크버튼 클릭시
        binding.createPartyTogetherCheckBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){ //체크를 하면
                binding.createPartyTogetherTv.setTextColor(ContextCompat.getColor(this, R.color.main))
            }else{ //체크 해제하면
                binding.createPartyTogetherTv.setTextColor(ContextCompat.getColor(this,R.color.gray_2))
            }
        }

        binding.createPartyTogetherTv.setOnClickListener {
            binding.createPartyTogetherCheckBtn.isChecked = !binding.createPartyTogetherCheckBtn.isChecked
            //체크되어있었으면 해제, 안체크 되어있었으면 체크 시키기
        }

        //주문예정시간 tv 클릭시
        binding.createPartyDate2Tv.setOnClickListener {
            Log.d("click","클릭됨")

            //파티생성하기 프레그먼트에서 다이얼로그로 전환
            /* val intent = Intent(context, CreatePartyDateDialogActivity::class.java)
             startActivity(intent)*/

            //fragment에서 커스텀 다이얼로그 띄우는 코드
            /*val dialog = DialogDt()
            dialog.show(parentFragmentManager, "CustomDialog")*/

            //activity에서 커스텀 다이얼로그 띄우는 코드
            DialogDt().show(
                supportFragmentManager, "CustomDialog"
            )
        }

    }
}