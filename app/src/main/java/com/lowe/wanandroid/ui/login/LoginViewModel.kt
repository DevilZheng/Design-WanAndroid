package com.lowe.wanandroid.ui.login

import androidx.lifecycle.MutableLiveData
import com.lowe.wanandroid.account.LocalUserInfo
import com.lowe.wanandroid.services.ApiResponse
import com.lowe.wanandroid.services.model.User
import com.lowe.wanandroid.services.repository.AccountRepository
import com.lowe.wanandroid.ui.BaseViewModel
import com.lowe.wanandroid.ui.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val accountRepository: AccountRepository) :
    BaseViewModel() {

    val loginLiveData = MutableLiveData<ApiResponse<User>>()

    fun login(userInfo: LocalUserInfo) {
        launch({
            loginLiveData.value = accountRepository.login(userInfo)
        })
    }
}