package com.lowe.wanandroid.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.lowe.common.base.BaseViewModel
import com.lowe.common.base.http.adapter.getOrElse
import com.lowe.common.services.model.Classify
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(private val repository: GroupRepository) :
    BaseViewModel() {

    val authorsNameLiveData: LiveData<List<Classify>> = liveData {
        emit(
            repository.getAuthorTitleList().getOrElse { emptyList() }
        )
    }
    private val _parentRefreshLiveData = MutableLiveData<Int>()
    val parentRefreshLiveData: LiveData<Int> = _parentRefreshLiveData
    private val _scrollToTopLiveData = MutableLiveData<Int>()
    val scrollToTopLiveData: LiveData<Int> = _scrollToTopLiveData

    /**
     * 用于公众号子Fragment滚到顶部
     */
    fun scrollToTopEvent(id: Int) {
        _scrollToTopLiveData.value = id
    }

    /**
     * 用于公众号子Fragment触发刷新
     */
    fun parentRefreshEvent(id: Int) {
        _parentRefreshLiveData.value = id
    }
}