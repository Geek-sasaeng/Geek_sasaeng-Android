package com.example.geeksasaeng.Home.Delivery

import android.util.Log
import com.bumptech.glide.Glide
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentBannerBinding

class BannerFragment(val imgUrl: String) : BaseFragment<FragmentBannerBinding>(FragmentBannerBinding::inflate) {
    override fun initAfterBinding() {
       /* binding.bannerImageIv.setImageResource(imgUrl)*/
        Log.d("BannerFragment", "Glide 실행됨")
        Glide.with(this)
            .load(imgUrl)
            .into(binding.bannerImageIv)
    }
}