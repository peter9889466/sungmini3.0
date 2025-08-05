package com.example.hackathon.ui.withdraw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WithdrawViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "회원탈퇴"
    }
    val text: LiveData<String> = _text

    private val _withdrawReason = MutableLiveData<String>()
    val withdrawReason: LiveData<String> = _withdrawReason

    private val _isAgreed = MutableLiveData<Boolean>()
    val isAgreed: LiveData<Boolean> = _isAgreed

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setWithdrawReason(reason: String) {
        _withdrawReason.value = reason
    }

    fun setAgreement(agreed: Boolean) {
        _isAgreed.value = agreed
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
}