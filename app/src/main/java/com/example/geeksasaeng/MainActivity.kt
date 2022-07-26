package com.example.geeksasaeng

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.geeksasaeng.Chatting.ChattingFragment
import com.example.geeksasaeng.Community.CommunityFragment
import com.example.geeksasaeng.Home.HomeFragment
import com.example.geeksasaeng.Home.Party.LookPartyFragment
import com.example.geeksasaeng.Profile.ProfileFragment
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    var status: String? = null
    var deliveryItemId: String? = null

    override fun initAfterBinding() {

        status = intent.getStringExtra("status")
        deliveryItemId = intent.getStringExtra("deliveryItemId")

        if (status == "search") {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("deliveryItemId", deliveryItemId)
            bundle.putString("status", status)

            val lookPartyFragment = LookPartyFragment()
            lookPartyFragment.arguments = bundle

            transaction.addToBackStack("lookParty").replace(R.id.main_frm, lookPartyFragment)
            transaction.commit()
        } else setFragment(R.id.main_frm, HomeFragment())

        //getAppKeyHash() //카카오맵 해시키 얻는 용
        setBottomNavi()
    }

    //해시키 얻는 코드드
/*   private fun getAppKeyHash() {
        try {
            val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("Hash key", something)
            }
        } catch (e: Exception) {

            Log.e("name not found", e.toString())
        }
    }*/

    private fun setBottomNavi() {
        binding.mainBottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFrament -> {
                    setFragment(R.id.main_frm, HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.communityFragment -> {
                    setFragment(R.id.main_frm, CommunityFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.chattingFragment -> {
                    setFragment(R.id.main_frm, ChattingFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.profileFragment -> {
                    setFragment(R.id.main_frm, ProfileFragment())
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

        binding.mainBottomNavi.itemIconTintList = null
    }
}