package com.example.geeksasaeng.Home.Search

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
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
            supportFragmentManager.beginTransaction().replace(R.id.search_frm, SearchDetailFragment()).addToBackStack("SearchDetail").commit()
        }
    }
}