package com.lowe.wanandroid.ui.coin.ranking

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.lowe.common.base.BaseViewModel
import com.lowe.common.base.IntKeyPagingSource
import com.lowe.common.base.http.adapter.getOrNull
import com.lowe.common.services.CoinService
import javax.inject.Inject

class CoinRankingRepository @Inject constructor(private val service: CoinService) {

    val coinRankingFlow = Pager(
        PagingConfig(
            pageSize = BaseViewModel.DEFAULT_PAGE_SIZE,
            initialLoadSize = BaseViewModel.DEFAULT_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        IntKeyPagingSource(service = service) { service, page, _ ->
            service.getCoinRanking(page).getOrNull()?.datas ?: emptyList()
        }
    }.flow

}