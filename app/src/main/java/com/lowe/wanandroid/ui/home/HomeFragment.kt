package com.lowe.wanandroid.ui.home

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.lowe.wanandroid.R
import com.lowe.wanandroid.databinding.FragmentHomeBinding
import com.lowe.wanandroid.ui.BaseFragment
import com.lowe.wanandroid.ui.home.item.HomeArticleItemBinder
import com.lowe.wanandroid.ui.home.repository.HomeViewModel
import com.lowe.wanandroid.utils.loadMore

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {

        private const val KEY_HOME_FRAGMENT_LIST_SAVE_STATE = "key_home_fragment_list_save_state"

    }

    private val homeAdapter = MultiTypeAdapter()

    override fun init(savedInstanceState: Bundle?) {
        initView()
        initObserve()
        if (savedInstanceState == null) {
            onRefresh()
        } else {
            viewBinding.homeList.layoutManager?.onRestoreInstanceState(
                savedInstanceState.getParcelable(KEY_HOME_FRAGMENT_LIST_SAVE_STATE)
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            KEY_HOME_FRAGMENT_LIST_SAVE_STATE,
            viewBinding.homeList.layoutManager?.onSaveInstanceState()
        )
    }

    private fun initView() {
        homeAdapter.register(HomeArticleItemBinder())
        viewBinding.homeList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = homeAdapter
            loadMore(loadFinish = { viewModel.isLoading.not() }) {
                viewModel.fetchArticleList()
            }
        }
        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            onRefresh()
        }
    }

    private fun initObserve() {
        viewModel.apply {
            articleListLiveData.observe(viewLifecycleOwner) {
                it?.let(this@HomeFragment::afterLoadArticle)
            }
        }
    }

    private fun afterLoadArticle(result: Pair<List<Any>, DiffUtil.DiffResult>) {
        homeAdapter.items = result.first
        result.second.dispatchUpdatesTo(homeAdapter)
    }

    private fun onRefresh() {
        viewBinding.swipeRefreshLayout.isRefreshing = true
        viewModel.refreshArticleList()
        viewBinding.swipeRefreshLayout.isRefreshing = false
    }

    override fun createViewModel() = HomeViewModel()
}