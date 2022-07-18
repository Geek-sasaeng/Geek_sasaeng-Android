package com.example.geeksasaeng.Home.CreateParty

import android.util.Log
import com.example.geeksasaeng.Utils.BaseFragment
import androidx.core.content.ContextCompat
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.FragmentCreatePartyBinding


class CreatePartyFragment : BaseFragment<FragmentCreatePartyBinding>(FragmentCreatePartyBinding::inflate) {

    override fun initAfterBinding() {
        initClickListener()
    }

    override fun onResume() {
        super.onResume()
        //fragment에서 받는다.
        Log.d("dialog", arguments?.getString("DateDialog")+" found")
        if(arguments!=null){
            binding.createPartyDate2Tv.text = arguments?.getString("DateDialog") // 파티생성 프래그먼트의 날짜부분의 text값 바꿔주기
        }
    }

    private fun initClickListener(){

        //같이 먹고 싶어요 체크버튼 클릭시
        binding.createPartyTogetherCheckBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){ //체크를 하면
                binding.createPartyTogetherTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.main))
            }else{ //체크 해제하면
                binding.createPartyTogetherTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_2))
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

            val dialog = DialogDt()
            dialog.show(parentFragmentManager, "CustomDialog")
        }


    }

}