package com.example.geeksasaeng.Home.Search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Party.LookPartyFragment
import com.example.geeksasaeng.MainActivity
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.databinding.ActivitySearchBinding

class SearchActivity: BaseActivity<ActivitySearchBinding>(ActivitySearchBinding::inflate) {

    override fun initAfterBinding() {
        initClickListener()

        supportFragmentManager.beginTransaction().replace(R.id.search_frm, SearchBasicFragment()).commit()

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (binding.searchEt.length() == 0) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.search_frm, SearchBasicFragment())
                        .addToBackStack("SearchBasic").commit()
                }
            }
        })
    }

    fun initClickListener() {
        binding.searchBtn.setOnClickListener {
            supportFragmentManager.popBackStack("SearchDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE)

            var keyword = binding.searchEt.text.toString()

            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            val bundle = Bundle()
            bundle.putString("keyword", keyword)

            val searchDetailFragment = SearchDetailFragment()
            searchDetailFragment.arguments = bundle

            transaction.addToBackStack("SearchDetail").replace(R.id.search_frm, searchDetailFragment)
            transaction.commit()
        }
    }
}