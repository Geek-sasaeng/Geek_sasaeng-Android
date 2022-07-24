package com.example.geeksasaeng.Home.CreateParty

import androidx.lifecycle.MutableLiveData
import net.daum.mf.map.api.MapPoint

class CreatePartyViewModel {
    var title: MutableLiveData<String?> = MutableLiveData<String?>() //제목
    var content: MutableLiveData<String?> = MutableLiveData<String?>() //내용
    var date: MutableLiveData<String?> = MutableLiveData<String?>() //날짜
    var time: MutableLiveData<String?> = MutableLiveData<String?>() //시간
    var maxMatching: MutableLiveData<Int?> = MutableLiveData<Int?>() //매칭인원수
    var category: MutableLiveData<String?> = MutableLiveData<String?>() //카테고리
    var storeUrl: MutableLiveData<String?> = MutableLiveData<String?>() //식당링크
    var mapPoint: MutableLiveData<MapPoint?> = MutableLiveData<MapPoint?>()
}