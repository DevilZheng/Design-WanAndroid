package com.lowe.wanandroid.ui.share

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.lowe.common.base.BaseViewModel
import com.lowe.common.base.IntKeyPagingSource
import com.lowe.common.base.http.adapter.NetworkResponse
import com.lowe.common.base.http.adapter.getOrNull
import com.lowe.common.base.http.adapter.whenSuccess
import com.lowe.common.services.ProfileService
import com.lowe.common.services.model.ShareBean
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class ShareListRepository @Inject constructor(
    private val profileService: ProfileService
) {

    private val _shareBeanFlow = MutableSharedFlow<ShareBean>(1)
    val shareBeanFlow: SharedFlow<ShareBean> = _shareBeanFlow

    fun getShareList(userId: String, isMe: Boolean) = Pager(
        PagingConfig(
            pageSize = BaseViewModel.DEFAULT_PAGE_SIZE,
            initialLoadSize = BaseViewModel.DEFAULT_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        IntKeyPagingSource(service = profileService) { profileService, page, _ ->
            val result: NetworkResponse<ShareBean> = if (isMe) {
                profileService.getMyShareList(page)
            } else {
                profileService.getUserShareList(userId, page)
            }
            result.whenSuccess { _shareBeanFlow.emit(it) }
            result.getOrNull()?.shareArticles?.datas ?: emptyList()
        }
    }.flow

}