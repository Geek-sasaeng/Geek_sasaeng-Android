package com.example.geeksasaeng.Base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding>(private val inflate: (LayoutInflater) -> T): AppCompatActivity(){

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun changeActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun setFragment(id: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(id, fragment)
            .commit()
    }
}