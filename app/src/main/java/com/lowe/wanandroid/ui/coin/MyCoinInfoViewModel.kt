package com.lowe.wanandroid.ui.coin

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lowe.common.account.IAccountViewModelDelegate
import com.lowe.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyCoinInfoViewModel @Inject constructor(
    repository: CoinRepository,
    accountViewModelDelegate: IAccountViewModelDelegate
) :
    BaseViewModel(), IAccountViewModelDelegate by accountViewModelDelegate {

    val userBaseInfoFlow = accountViewModelDelegate.accountInfo

    val coinHistoryFlow = repository.getCoinHistoryFlow().cachedIn(viewModelScope)

}