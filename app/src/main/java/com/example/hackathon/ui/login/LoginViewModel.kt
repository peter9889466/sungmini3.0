package com.example.hackathon.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is login Fragment"
    }
    val text: LiveData<String> = _text

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    private val _userPw = MutableLiveData<String>()
    val userPw: LiveData<String> = _userPw

    fun setUserInfo(id: String, pw: String) {
        _userId.value = id
        _userPw.value = pw
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
}