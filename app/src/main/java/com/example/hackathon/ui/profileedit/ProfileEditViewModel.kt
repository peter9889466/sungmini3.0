package com.example.hackathon.ui.profileedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileEditViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "회원정보 수정"
    }
    val text: LiveData<String> = _text

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> = _userEmail

    private val _userPhone = MutableLiveData<String>()
    val userPhone: LiveData<String> = _userPhone

    fun setUserInfo(name: String, email: String, phone: String) {
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