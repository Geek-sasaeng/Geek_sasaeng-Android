package com.example.geeksasaeng.Home.Search

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geeksasaeng.Home.Delivery.Adapter.DeliveryRVAdapter
import com.example.geeksasaeng.Home.Delivery.Adapter.PeopleSpinnerAdapter
import com.example.geeksasaeng.Home.Delivery.DeliveryPartiesVoList
import com.example.geeksasaeng.Home.Delivery.DeliveryResult
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Utils.BaseFragment
import com.example.geeksasaeng.databinding.FragmentSearchBasicBinding
import com.example.geeksasaeng.databinding.FragmentSearchDetailBinding

class SearchDetailFragment: BaseFragment<FragmentSearchDetailBinding>(FragmentSearchDetailBinding::inflate) {

    private var deliveryArray = ArrayList<DeliveryPartiesVoList?>()
    private lateinit var deliveryAdapter: DeliveryRVAdapter

    override fun initAfterBinding() {
        initAdapter()
        initSpinner()

        deliveryArray.apply {
            add(DeliveryPartiesVoList(2, "한식", 100, 4, "2022-07-22 01:20:48", "DummyData", true))
            add(DeliveryPartiesVoList(2, "한식", 100, 4, "2022-07-22 01:20:48", "DummyData", true))
            add(DeliveryPartiesVoList(2, "한식", 100, 4, "2022-07-22 01:20:48", "DummyData", true))
            add(DeliveryPartiesVoList(2, "한식", 100, 4, "2022-07-22 01:20:48", "DummyData", true))
            add(DeliveryPartiesVoList(2, "한식", 100, 4, "2022-07-22 01:20:48", "DummyData", true))
            add(DeliveryPartiesVoList(2, "한식", 100, 4, "2022-07-22 01:20:48", "DummyData", true))
            add(DeliveryPartiesVoList(2, "한식", 100, 4, "2022-07-22 01:20:48", "DummyData", true))
        }
    }

    private fun initAdapter() {
        deliveryAdapter = DeliveryRVAdapter(deliveryArray)
        binding.searchDetailPartyRv.adapter = deliveryAdapter
        binding.searchDetailPartyRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    //스피너 관련 작업
    private fun initSpinner(){
        val items = resources.getStringArray(R.array.home_dropdown1) // spinner아이템 배열
        //어댑터
        val spinnerAdapter = PeopleSpinnerAdapter(requireContext(), items)
        binding.searchDetailPeopleSpinner.adapter = spinnerAdapter
        binding.searchDetailPeopleSpinner.setSelection(items.size - 1) //마지막아이템을 스피너 초기값으로 설정해준다.

        //이벤트 처리
        binding.searchDetailPeopleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //TODO:스피너
                //축소된 스피너화면에 맞게 아이템 색상, 화살표 변경
                val image: ImageView = view!!.findViewById(R.id.arrow_iv)
                image.setImageResource(R.drawable.ic_spinner_up)
                image.visibility = View.VISIBLE
                items[0]=items[position] // items[0]은 현재 선택된 아이템 저장용
                val textName: TextView = view!!.findViewById(R.id.spinner_text)
                textName.text = items[position]
                textName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_2))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }
}