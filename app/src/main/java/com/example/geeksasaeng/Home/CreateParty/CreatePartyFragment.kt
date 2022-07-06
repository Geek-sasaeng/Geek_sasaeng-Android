package com.example.geeksasaeng.Home.CreateParty

import android.util.Log
import com.example.geeksasaeng.Base.BaseFragment
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