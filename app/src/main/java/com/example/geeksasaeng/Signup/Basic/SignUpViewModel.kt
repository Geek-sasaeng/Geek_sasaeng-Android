package com.example.geeksasaeng.Signup.Basic

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel: ViewModel() {
    var checkPassword: MutableLiveData<String?> = MutableLiveData<String?>()
    var loginId: MutableLiveData<String?> = MutableLiveData<String?>()
    var nickname: MutableLiveData<String?> = MutableLiveData<String?>()
    var password: MutableLiveData<String?> = MutableLiveData<String?>()
    var email: MutableLiveData<String?> = MutableLiveData<String?>()
    var emailId: MutableLiveData<Int?> = MutableLiveData<Int?>()
    var universityName: MutableLiveData<String?> = MutableLiveData<String?>()
    var phoneNumber: MutableLiveData<String?> = MutableLiveData<String?>()
    var phoneNumberId: MutableLiveData<Int?> = MutableLiveData<Int?>()
    var informationAgreeStatus: MutableLiveData<String?> = MutableLiveData<String?>()

    fun setCheckPassword(value: String?) {
        checkPassword.value = value
    }

    fun getCheckPassword(): String? {
        return checkPassword.value.toString()
    }

    fun setLoginId(value: String?) {
        loginId.value = value
    }

    fun getLoginId(): String {
        return loginId.value.toString()
    }

    fun setNickname(value: String?) {
        nickname.value = value
    }

    fun getNickname(): String {
        return nickname.value.toString()
    }

    fun setPassword(value: String?) {
        password.value = value
    }

    fun getPassword(): String? {
        return password.value.toString()
    }

    fun setEmail(value: String?) {
        email.value = value
    }

    fun getEmail() : String? {
        return email.value.toString()
    }

    fun setEmailId(value: Int?) {
        emailId.value = value
    }

    fun getEmailId(): Int {
        return Integer.parseInt(emailId.value.toString())
    }

    fun setUniversityName(value: String?) {
        universityName.value = value
    }

    fun getUniversityName(): String {
        return universityName.value.toString()
    }

    fun setPhoneNumber(value: String?) {
        phoneNumber.value = value
    }

    fun getPhoneNumber() : String {
        return phoneNumber.value.toString()
    }

    fun setPhoneNumberId(value: Int?) {
        phoneNumberId.value = value
    }

    fun getPhoneNumberId(): Int? {
        return phoneNumberId.value
    }

    fun setInformationAgreeStatus(value: String?) {
        informationAgreeStatus.value = value
    }

    fun getInformationAgreeStatus() : String {
        return informationAgreeStatus.value.toString()
    }
}