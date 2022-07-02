package com.lowe.wanandroid.ui.search.begin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lowe.wanandroid.di.IOApplicationScope
import com.lowe.wanandroid.services.model.HotKeyBean
import com.lowe.wanandroid.services.model.success
import com.lowe.wanandroid.ui.BaseViewModel
import com.lowe.wanandroid.ui.launch
import com.lowe.wanandroid.ui.search.SearchRepository
import com.lowe.wanandroid.ui.search.SearchState
import com.lowe.wanandroid.widgets.LimitedLruQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBeginViewModel @Inject constructor(
    private val repository: SearchRepository,
    @IOApplicationScope private val applicationScope: CoroutineScope
) :
    BaseViewModel() {

    private val _searchHotKeyLiveData = MutableLiveData<List<HotKeyBean>>()
    val searchHotKeyLiveData: LiveData<List<HotKeyBean>> = _searchHotKeyLiveData
    private val _historyLiveData = MutableLiveData<List<SearchState>>()
    val historyLiveData: LiveData<List<SearchState>> = _historyLiveData

    /**
     * 保存搜查记录的LRUCache
     */
    private val historyLruCache = LimitedLruQueue<SearchState>(20)

    override fun init() {
        super.init()
        getHotKeys()
    }

    override fun onCleared() {
        super.onCleared()
        /**
         * 更新搜索记录，在ViewModel生命周期结束后触发，所以需要applicationScope来开启协程
         */
        applicationScope.launch {
            repository.updateSearchHistory(historyLruCache.map { it.keywords }.toSet())
        }
    }

    private fun getHotKeys() {
        launch({
            _searchHotKeyLiveData.value = repository.getHotKeyList().success()?.data ?: emptyList()
        })
    }

    fun searchHistoryFlow() = repository.searchHistoryCache()

    fun initHistoryCache(histories: List<SearchState>) {
        historyLruCache.addAll(histories)
    }

    fun historyPut(state: SearchState) {
        historyLruCache.add(state)
        _historyLiveData.value = historyLruCache.toList()
    }

    fun historyRemove(state: SearchState) {
        historyLruCache.remove(state)
        _historyLiveData.value = historyLruCache.toList()
    }

}