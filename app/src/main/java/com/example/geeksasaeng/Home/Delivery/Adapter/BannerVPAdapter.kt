package com.example.geeksasaeng.Home.Delivery.Adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment:Fragment) :FragmentStateAdapter(fragment) {

    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = Int.MAX_VALUE //무한 스크롤

    override fun createFragment(position: Int): Fragment = fragmentlist[position%fragmentlist.size]

    fun addFragment(fragment: Fragment){
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size-1)
    }

}