package com.example.geeksasaeng.Home.Delivery

import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentBannerBinding

class BannerFragment(val imgRes: Int) : BaseFragment<FragmentBannerBinding>(FragmentBannerBinding::inflate) {
    override fun initAfterBinding() {
        binding.bannerImageIv.setImageResource(imgRes)
    }
}