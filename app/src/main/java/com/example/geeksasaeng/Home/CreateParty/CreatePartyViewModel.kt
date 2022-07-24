package com.example.geeksasaeng.Home.CreateParty

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.daum.mf.map.api.MapPoint

//TODO: category랑 categoryInt랑 합쳐보기 (7.30.~7.31.)
class CreatePartyViewModel: ViewModel() {
    /*var hashTag :  MutableLiveData<Boolean?> = MutableLiveData<Boolean?>() // 해시태그 여부
    var title: MutableLiveData<String?> = MutableLiveData<String?>() //제목
    var content: MutableLiveData<String?> = MutableLiveData<String?>() //내용*/
    var date: MutableLiveData<String?> = MutableLiveData<String?>() //날짜
    var time: MutableLiveData<String?> = MutableLiveData<String?>() //시간
    var dateTime : MutableLiveData<String?> = MutableLiveData<String?>() //API용 날짜+시간
    var maxMatching: MutableLiveData<Int?> = MutableLiveData<Int?>() //매칭인원수
    var category: MutableLiveData<String?> = MutableLiveData<String?>() //카테고리
    var categoryInt: MutableLiveData<Int?> = MutableLiveData<Int?>() //카테고리
    var storeUrl: MutableLiveData<String?> = MutableLiveData<String?>() //식당링크
    var mapPoint: MutableLiveData<MapPoint?> = MutableLiveData<MapPoint?>() //위치

/*    fun setHashTag(value: Boolean?){
        hashTag.value = value
    }

    fun getHashTag() : Boolean?{
        return hashTag.value
    }

    fun setTitle(value: String?){
        title.value = value
    }

    fun getTitle() : String?{
        return title.value.toString()
    }

    fun setContent(value: String?){
        content.value = value
    }

    fun getContent() : String?{
        return content.value.toString()
    }*/

    fun setDate(value: String?){
        date.value = value
    }

    fun getDate() : String?{
        return date.value.toString()
    }

    fun setTime(value: String?){
        time.value = value
    }

    fun getTime() : String?{
        return time.value.toString()
    }

    fun setDateTime(value: String?){
        dateTime.value = value
    }

    fun getDateTime() : String?{
        return dateTime.value.toString()
    }

    fun setMaxMatching(value: Int?){
        maxMatching.value = value
    }

    fun getMaxMatching() : Int?{
        return maxMatching.value
    }

    fun setCategory(value: String?){
        //TODO: 솔직히 너무 하드코딩 & 비효율적
        category.value = value
        when(value.toString()){
            "한식"-> setCategoryInt(1)
            "중식"-> setCategoryInt(2)
            "분식"-> setCategoryInt(3)
            "회/돈까스"-> setCategoryInt(4)
            "디저트/음료"-> setCategoryInt(5)
            "양식"-> setCategoryInt(6)
            "일식"-> setCategoryInt(7)
            "치킨/피자"-> setCategoryInt(8)
            "패스트 푸드"-> setCategoryInt(9)
            "기타"-> setCategoryInt(10)
            else->{}
        }
    }

    fun getCategory() : String?{
        return category.value.toString()
    }

    fun setCategoryInt(value: Int?){
        categoryInt.value = value
    }

    fun getCategoryInt() : Int?{
        return categoryInt.value
    }

    fun setStoreUrl(value: String?){
        storeUrl.value = value
    }

    fun getStoreUrl() : String?{
        return storeUrl.value.toString()
    }

    fun setMapPoint(value: MapPoint?){
        mapPoint.value = value
    }

    fun getMapPoint() : MapPoint?{
        return mapPoint.value
    }


}