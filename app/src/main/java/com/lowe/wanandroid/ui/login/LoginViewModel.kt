package com.lowe.wanandroid.ui.login

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lowe.common.account.IAccountViewModelDelegate
import com.lowe.common.account.LocalUserInfo
import com.lowe.common.account.RegisterInfo
import com.lowe.common.base.BaseViewModel
import com.lowe.common.base.http.adapter.NetworkResponse
import com.lowe.common.services.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountViewModelDelegate: IAccountViewModelDelegate
) :
    BaseViewModel(), IAccountViewModelDelegate by accountViewModelDelegate {

    private val _loginLiveData = MutableLiveData<NetworkResponse<User>>()
    val loginLiveData: LiveData<NetworkResponse<User>> = _loginLiveData

    private val _registerLiveData = MutableLiveData<NetworkResponse<Any>>()
    val registerLiveData: LiveData<NetworkResponse<Any>> = _registerLiveData

    /**
     * 数据绑定
     */
    val userNameObservable = ObservableField<String>()
    val passwordObservable = ObservableField<String>()
    val loginEnable = object : ObservableBoolean(userNameObservable, passwordObservable) {

        override fun get() =
            userNameObservable.get()?.trim().isNullOrBlank().not() && passwordObservable.get()
                ?.trim().isNullOrBlank().not()
    }

    fun userLogin(userInfo: LocalUserInfo) {
        viewModelScope.launch {
            _loginLiveData.value = accountViewModelDelegate.login(userInfo)
        }
    }

    fun userRegister(registerInfo: RegisterInfo) {
        viewModelScope.launch {
            _registerLiveData.value = accountViewModelDelegate.register(registerInfo)
        }
    }
}