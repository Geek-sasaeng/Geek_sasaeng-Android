package com.example.geeksasaeng

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.Data.Signup
import com.example.geeksasaeng.Signup.*
import com.example.geeksasaeng.Signup.Retrofit.SignupDataService
import com.example.geeksasaeng.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {

    private lateinit var progressVM: ProgressViewModel

    override fun initAfterBinding() {
        progressVM = ViewModelProvider(this).get(ProgressViewModel::class.java)

        supportFragmentManager.beginTransaction()
            .replace(R.id.sign_up_vp, StepOneFragment())
            .commit()

        initClickListener()
    }

    private fun initClickListener() {
        progressVM.currentPro.observe(this, Observer {
            binding.signUpProgressbar.setProgress(progressVM.currentPro.value!!.toInt())
            Log.d("PROGRESS-STATUS", "${progressVM.currentPro.value!!.toInt()}")
        })

//        progressVM.currentPro.observe(this, Observer {
//            progressVM.currentPro = progressVM.currentPro + 1
//        })

        /*
        binding.signUpNextBtn.setOnClickListener { //다음버튼의 클릭이벤트 처리
            var currentPos= binding.signUpVp.currentItem

            if (currentPos==2){ //마지막 페이지
                //회원가입 진행
            }else{ //마지막 페이지가 아니면 다음 페이지로 넘겨주기
                if(currentPos==1){ //이때 대마지막 전페이지면
                    binding.signUpNextBtn.text = "시작하기"
                }
                binding.signUpVp.setCurrentItem(currentPos+1, false)
            }
            binding.signUpProgressbar.setProgress(currentPos+1) // 프로그레스바 진행률 올리기
        }
        */
    }
}