package com.example.geeksasaeng.Base.Example

import com.example.geeksasaeng.Base.BaseFragment
import com.example.geeksasaeng.databinding.FragmentExampleBinding

class ExampleFragment(): BaseFragment<FragmentExampleBinding>(FragmentExampleBinding::inflate) {
    override fun initAfterBinding() {
        //
    }
}