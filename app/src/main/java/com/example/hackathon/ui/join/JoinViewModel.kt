package com.example.hackathon.ui.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JoinViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is join Fragment"
    }
    val text: LiveData<String> = _text

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    private val _userPw = MutableLiveData<String>()
    val userPw: LiveData<String> = _userPw
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> = _userEmail

    private val _userPhone = MutableLiveData<String>()
    val userPhone: LiveData<String> = _userPhone

    fun setUserInfo(id:String, pw:String, name: String, email: String, phone: String) {
        _userId.value = id
        _userPw.value = pw
        _userName.value = name
        _userEmail.value = email
        _userPhone.value = phone
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
}