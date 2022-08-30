package com.example.geeksasaeng.Home.Delivery.Adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.geeksasaeng.Home.Delivery.BannerFragment

class BannerVPAdapter(fragment:Fragment) :FragmentStateAdapter(fragment) {

    private val fragmentList : ArrayList<BannerFragment> = ArrayList()

    override fun getItemCount(): Int = Int.MAX_VALUE //무한 스크롤

    override fun createFragment(position: Int): BannerFragment = fragmentList[position%fragmentList.size]

    fun addFragment(imgUrl: String){
        val bundle = Bundle()
        bundle.putString("imgUrl", imgUrl)
        val frag = BannerFragment()
        frag.arguments = bundle
        fragmentList.add(fragmentList.size, frag)
        notifyItemInserted(fragmentList.size-1)
    }

}