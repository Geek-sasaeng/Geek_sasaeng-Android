package com.example.geeksasaeng.Home.Search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseActivity
import com.example.geeksasaeng.Utils.saveRecentSearch
import com.example.geeksasaeng.databinding.ActivitySearchBinding

class SearchActivity: BaseActivity<ActivitySearchBinding>(ActivitySearchBinding::inflate) {

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("searchFlag", "뒤로가기 눌림")
    }

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
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE) //이거 안하면 뒤로가기 2번 눌러야되더라.
                }
            }
        })
    }

    private fun initClickListener() {
        binding.searchBtn.setOnClickListener {
            var keyword = binding.searchEt.text.toString()
            var keywordCheck = keyword.replace(" ", "")

            if (keywordCheck.isEmpty()) {
                showToast("검색어를 입력해주세요")
            } else {
                saveRecentSearch(keyword)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

                supportFragmentManager.popBackStack("SearchDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE)

                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

                val bundle = Bundle()
                bundle.putString("keyword", keyword)

                val searchDetailFragment = SearchDetailFragment()
                searchDetailFragment.arguments = bundle

                transaction.addToBackStack("SearchDetail").replace(R.id.search_frm, searchDetailFragment)
                transaction.commit()
            }
        }

        // 엔터치면 검색되게 하는 기능
        binding.searchEt.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                binding.searchBtn.performClick()
                true
            } else false
        }
    }

    fun doSearch(keyword: String){
        binding.searchEt.setText(keyword)
        binding.searchBtn.performClick()
    }
}
