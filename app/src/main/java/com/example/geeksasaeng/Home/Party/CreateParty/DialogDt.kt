package com.example.geeksasaeng.Home.Party.CreateParty

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.geeksasaeng.Home.CreateParty.CreatePartyViewModel
import com.example.geeksasaeng.R
import com.example.geeksasaeng.Signup.Basic.SignUpViewModel
import com.example.geeksasaeng.databinding.DialogDtLayoutBinding
import java.util.*

class DialogDt : DialogFragment() {
    lateinit var binding: DialogDtLayoutBinding
    private var dialogDtNextClickListener: DialogDtNextClickListener? =null
    var dateString = ""
    var dateString2 = ""
    var timeString = ""
    var timeString2 = ""

    private val createPartyVM: CreatePartyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DialogDtLayoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //레이아웃배경을 투명하게 해줌?
        initData()
        initClickListener()
        return binding.root
    }

    private fun initData(){
        //사용자가 입력해둔 정보 있으면, 그걸 default값으로 설정해주기 위함 (사용자가 입력해둔 정보 유무는 ViewModel에 저장되어 있는지 여부로 결정)
        if((createPartyVM.getDate().toString()!="null") && (createPartyVM.getTime().toString()!="null")){
            Log.d("dialogDT", createPartyVM.getDate().toString()+createPartyVM.getTime().toString())
            dateString =createPartyVM.getDate().toString()
            timeString =createPartyVM.getTime().toString()
            binding.dateDialogDateTv.text = createPartyVM.getDate().toString()
            binding.dateDialogTimeTv.text = createPartyVM.getTime().toString()
        }
    }

    override fun onResume() {
        super.onResume()

        //<방법1: 디바이스 크기에 비례해서 다이얼로그 크기 조정>
        /*val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params : ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams*/

        //<방법2: 지정해둔 dp크기로로 다이얼로그 기 조정>
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog?.window?.setLayout(width,height)
    }

    interface DialogDtNextClickListener{
        //TODO: 뷰모델 이용하면서 사실 여기서 매개변수로 안넘겨줘도 ACTIVITY에서 값 알 수 있어..
        //TODO: 근데 이걸 하는 이유는 정보 갱신을 위함.
        fun onDtClicked(date:String, time:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogDtNextClickListener = activity as DialogDtNextClickListener
    }

    override fun onDetach() {
        super.onDetach()
        dialogDtNextClickListener?.onDtClicked(dateString, timeString)//frag-> activity 정보전달
        createPartyVM.setDateTime(dateString2+" "+ timeString2) //TODO:이러면 근데 "" 빈 문자열이 저장될 수 도 있는뎅..
        dialogDtNextClickListener = null
    }

    private fun initClickListener(){
        binding.dateDialogDateTv.setOnClickListener { //날짜 정보
            //DatePickerApplicationClass
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${month+1}월 ${dayOfMonth}일"
                dateString2 = "${year}-${month}-${dayOfMonth}"
                Log.d("dialog", dateString)
                binding.dateDialogDateTv.text = dateString
                createPartyVM.setDate(dateString)
            }
            DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        binding.dateDialogTimeTv.setOnClickListener { //시간 정보
            val cal = Calendar.getInstance()   //캘린더뷰 만들기
            //TimePicker
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                timeString = "${hourOfDay}시 ${minute}분"
                timeString2 = "${hourOfDay}:${minute}:30" //TODO:임의로 그냥 초는 30초로 해둠.. 상관 없겠징? ㅎㅎ
                binding.dateDialogTimeTv.text = timeString
                createPartyVM.setTime(timeString)
            }
            TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()

        }


        binding.dateDialogNextBtn.setOnClickListener { //다음버튼

            //다음 다이얼로그 띄우기
            val dialogNum = DialogNum()
            dialogNum.show(parentFragmentManager, "CustomDialog")

            //자기는 종료
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)?.commit()
        }
    }

}