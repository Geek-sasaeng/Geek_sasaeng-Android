package com.example.geeksasaeng

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.geeksasaeng.SignUpActivity
import com.example.geeksasaeng.Signup.StepFourFragment
import com.example.geeksasaeng.Signup.StepOneFragment
import com.example.geeksasaeng.Signup.StepThreeFragment
import com.example.geeksasaeng.Signup.StepTwoFragment

class SignUpVPAdapter(fragment: SignUpActivity) :FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5 // signup 단계 5개이므로

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> StepOneFragment()
            1 -> StepTwoFragment()
            2 -> StepThreeFragment()
            else -> StepFourFragment()
        }
    }

}