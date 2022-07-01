package com.example.geeksasaeng

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.geeksasaeng.Base.BaseActivity
import com.example.geeksasaeng.databinding.ActivityCreatePartyDateDialogBinding
import com.example.geeksasaeng.databinding.FragmentCreatePartyBinding
import java.util.*


class CreatePartyDateDialogActivity : BaseActivity<ActivityCreatePartyDateDialogBinding>(ActivityCreatePartyDateDialogBinding::inflate) {

    var dateString = ""
    var timeString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initClickListener()
    }

    private fun initClickListener() {
        binding.dateDialogDateTv.setOnClickListener { //날짜 정보
            //DatePickerApplicationClass
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${month+1}월 ${dayOfMonth}일"
                Log.d("dialog", dateString)
                binding.dateDialogDateTv.text = dateString
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.dateDialogTimeTv.setOnClickListener { //시간 정보
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            //TimePicker
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                timeString = "${hourOfDay}시 ${minute}분"
                binding.dateDialogTimeTv.text = timeString
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()

        }

        binding.dateDialogNextBtn.setOnClickListener { //다음버튼

            //일단 테스트용용
           if(dateString!=""&&timeString!=""){
                Log.d("dialog", dateString)
                Log.d("dialog", timeString)

                val myFragment = CreatePartyFragment()
                var args = Bundle()
                args.putString("DateDialog", dateString + "  " +timeString)
                myFragment.arguments = args

               val fragmentTransaction = supportFragmentManager.beginTransaction()
               /*fragmentTransaction.replace(R.id.myframe, myFragment, "counter")*/
               fragmentTransaction.commit()

                finish()

            }
        }
    }

}
