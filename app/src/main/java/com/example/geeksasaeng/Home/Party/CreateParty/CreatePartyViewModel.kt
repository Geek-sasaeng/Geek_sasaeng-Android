package com.example.geeksasaeng.Home.CreateParty

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.daum.mf.map.api.MapPoint

//TODO: category랑 categoryInt랑 합쳐보기 (7.30.~7.31.)
class CreatePartyViewModel: ViewModel() {
    //var hashTag :  MutableLiveData<Boolean?> = MutableLiveData<Boolean?>() // 해시태그 여부
    var title: MutableLiveData<String?> = MutableLiveData<String?>() //제목
    var content: MutableLiveData<String?> = MutableLiveData<String?>() //내용
    var date: MutableLiveData<String?> = MutableLiveData<String?>() //날짜
    var time: MutableLiveData<String?> = MutableLiveData<String?>() //시간
    var date2 : MutableLiveData<String?> = MutableLiveData<String?>() //API용 날짜+시간
    var time2 : MutableLiveData<String?> = MutableLiveData<String?>() //API용 날짜+시간
    var maxMatching: MutableLiveData<Int?> = MutableLiveData<Int?>() //매칭인원수
    var category: MutableLiveData<String?> = MutableLiveData<String?>() //카테고리
    var categoryInt: MutableLiveData<Int?> = MutableLiveData<Int?>() //카테고리
    var storeUrl: MutableLiveData<String?> = MutableLiveData<String?>() //식당링크
    var mapPoint: MutableLiveData<MapPoint?> = MutableLiveData<MapPoint?>() //위치
    var account : MutableLiveData<String?> = MutableLiveData<String?>() //계좌은행 정보
    var accountNumber : MutableLiveData<String?> = MutableLiveData<String?>() //계좌은행 번호
    var partyName: MutableLiveData<String?> = MutableLiveData<String?>() //파티 이름


/*    fun setHashTag(value: Boolean?){
        hashTag.value = value
    }

    fun getHashTag() : Boolean?{
        return hashTag.value
    }*/

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
    }

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

    fun setDate2(value: String?){
        date2.value = value
    }

    fun getDate2() : String?{
        return date2.value.toString()
    }

    fun setTime2(value: String?){
        time2.value = value
    }

    fun getTime2() : String?{
        return time2.value.toString()
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
            "중식"-> setCategoryInt(3)
            "분식"-> setCategoryInt(5)
            "회/돈까스"-> setCategoryInt(7)
            "디저트/음료"-> setCategoryInt(9)
            "양식"-> setCategoryInt(2)
            "일식"-> setCategoryInt(4)
            "치킨/피자"-> setCategoryInt(6)
            "패스트 푸드"-> setCategoryInt(8)
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

    fun setAccount(value: String?){
        account.value = value
    }

    fun getAccount() : String?{
        return account.value.toString()
    }

    fun setAccountNumber(value: String?){
        accountNumber.value = value
    }

    fun getAccountNumber() : String?{
        return accountNumber.value.toString()
    }

    fun setPartyName(value: String?){
        partyName.value = value
    }

    fun getPartyName() : String?{
        return partyName.value.toString()
    }


}