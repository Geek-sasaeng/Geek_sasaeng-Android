package com.example.geeksasaeng.Home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.geeksasaeng.Home.Delivery.DeliveryFragment
import com.example.geeksasaeng.Home.Helper.HelperFragment
import com.example.geeksasaeng.Home.Market.MarketFragment

class HomeVPAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DeliveryFragment()
            1 -> MarketFragment()
            else -> HelperFragment()
        }
    }
}