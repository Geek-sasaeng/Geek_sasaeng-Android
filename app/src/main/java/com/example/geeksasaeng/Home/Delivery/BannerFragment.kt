package com.example.geeksasaeng.Home.Delivery

import android.util.Log
import com.bumptech.glide.Glide
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentBannerBinding

class BannerFragment() : BaseFragment<FragmentBannerBinding>(FragmentBannerBinding::inflate) {
    override fun initAfterBinding() {
        val imgUrl = requireArguments().getString("imgUrl")
        Glide.with(this)
            .load(imgUrl)
            .into(binding.bannerImageIv)
    }
}