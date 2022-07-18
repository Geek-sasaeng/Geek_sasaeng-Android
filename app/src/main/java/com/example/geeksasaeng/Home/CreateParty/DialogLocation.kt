package com.example.geeksasaeng.Home.CreateParty

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.geeksasaeng.BuildConfig
import com.example.geeksasaeng.R
import com.example.geeksasaeng.databinding.DialogLocationLayoutBinding
import net.daum.mf.map.api.MapView
import java.security.MessageDigest

class DialogLocation: DialogFragment() {

    lateinit var binding: DialogLocationLayoutBinding
    private var dialogLocationNextClickListener: DialogLocationNextClickListener? =null
    var LocationString = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DialogLocationLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        ininKakaoMap()
        initClickListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }

    //frag->Activity 정보전달용 코드 시작
    interface DialogLocationNextClickListener{
        fun onLocationClicked(text:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogLocationNextClickListener = activity as DialogLocationNextClickListener
    }

    override fun onDetach() {
        super.onDetach()
        dialogLocationNextClickListener = null
    }
    //frag->Activity 정보전달용 코드 끝

    private fun ininKakaoMap(){
        val mapView = MapView(activity)
        binding.locationDialogKakaoMapView.addView(mapView)
    }

    private fun initClickListener(){
        //다음버튼
        binding.locationDialogNextBtn.setOnClickListener {
            //마지막 페이지이므로 그냥 종료

            //frag-> activity 정보전달
            LocationString = binding.locationDialogSearchEt.text.toString()
            dialogLocationNextClickListener?.onLocationClicked(LocationString)

            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }

        //뒤로가기버튼
        binding.locationDialogBackBtn.setOnClickListener {

            //이전 다이얼로그 실행
            val dialogCategory = DialogCategory()
            dialogCategory.show(parentFragmentManager, "CustomDialog")

            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }
    }

}