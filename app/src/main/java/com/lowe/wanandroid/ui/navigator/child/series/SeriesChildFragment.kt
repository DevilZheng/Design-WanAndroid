package com.lowe.wanandroid.ui.navigator.child.series

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.lowe.wanandroid.BaseFragment
import com.lowe.common.services.model.Classify
import com.lowe.common.services.model.Series
import com.lowe.common.utils.smoothSnapToPosition
import com.lowe.multitype.MultiTypeAdapter
import com.lowe.wanandroid.R
import com.lowe.wanandroid.databinding.FragmentNavigatorChildSeriesBinding
import com.lowe.wanandroid.ui.navigator.NavigatorFragment
import com.lowe.wanandroid.ui.navigator.NavigatorTabBean
import com.lowe.wanandroid.ui.navigator.child.series.detail.SeriesDetailListActivity
import com.lowe.wanandroid.ui.navigator.child.series.item.SeriesChildTagChildrenItemBinder
import com.lowe.wanandroid.ui.navigator.widgets.NavigatorTagOnScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 体系在导航Tab下的子Fragment页面
 */
@AndroidEntryPoint
class SeriesChildFragment :
    BaseFragment<SeriesChildViewModel, FragmentNavigatorChildSeriesBinding>(R.layout.fragment_navigator_child_series) {

    companion object {
        fun newInstance(navigatorTabBean: NavigatorTabBean): SeriesChildFragment = with(
            SeriesChildFragment()
        ) {
            arguments =
                bundleOf(NavigatorFragment.KEY_NAVIGATOR_CHILD_HOME_TAB_PARCELABLE to navigatorTabBean)
            this
        }
    }

    private val tagChildrenAdapter = MultiTypeAdapter()
    private val tagOnScrollListener = NavigatorTagOnScrollListener()
    private val tagChildrenFirstCompletelyVisiblePosChange: Job =
        lifecycleScope.launch(start = CoroutineStart.LAZY) {
            tagOnScrollListener.firstCompletelyVisiblePosChange.collect(this@SeriesChildFragment::tagSelectedChange)
        }

    override val viewModel: SeriesChildViewModel by viewModels()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        initView()
        initEvents()
    }

    private fun initView() {
        viewDataBinding.apply {
            with(seriesTagChildrenList) {
                tagChildrenAdapter.register(SeriesChildTagChildrenItemBinder(this@SeriesChildFragment::onTagChildrenItemClick))
                adapter = tagChildrenAdapter
                layoutManager = LinearLayoutManager(context)
                addOnScrollListener(tagOnScrollListener)
            }
        }
    }

    private fun initEvents() {
        viewModel.apply {
            seriesListLiveData.observe(viewLifecycleOwner) {
                dispatchToAdapter(it, tagChildrenAdapter)
                generateVerticalScrollChipGroup(it.first)
                if (tagChildrenFirstCompletelyVisiblePosChange.isActive.not()) {
                    tagChildrenFirstCompletelyVisiblePosChange.start()
                }
                viewDataBinding.loadingContainer.loadingProgress.isVisible = false
            }
        }
    }

    private fun dispatchToAdapter(
        result: Pair<List<Any>, DiffUtil.DiffResult>,
        adapter: MultiTypeAdapter
    ) {
        adapter.items = result.first
        result.second.dispatchUpdatesTo(adapter)
    }

    private fun generateVerticalScrollChipGroup(tags: List<Any>) {
        viewDataBinding.seriesTagList.setViews(
            tags.map {
                val series = it as Series
                Chip(
                    this.requireContext(),
                    null,
                    com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
                ).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setChipBackgroundColorResource(R.color.choice_chip_background_color)
                    text = series.name
                    textSize = 13F
                    chipStartPadding = 0F
                    chipEndPadding = 0F
                    textStartPadding = 0F
                    textEndPadding = 0F
                    isCheckable = true
                    isCheckedIconVisible = false
                    gravity = Gravity.CENTER
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    if (paint.measureText(text.toString()) > viewDataBinding.seriesTagList.width) {
                        textSize = 11F
                    }
                }
            }.onEach { chip ->
                chip.setOnClickListener {
                    onTagClick(
                        viewDataBinding.seriesTagList.getChipGroup().indexOfChild(it)
                    )
                }
            }
        )
    }

    private fun onTagClick(pos: Int) {
        viewDataBinding.seriesTagChildrenList.smoothSnapToPosition(pos)
    }

    private fun onTagChildrenItemClick(classify: Classify) {
        val series = viewModel.getCurrentList()
            .find { it is Series && it.id == classify.parentChapterId } as? Series ?: return
        startActivity(Intent(this.context, SeriesDetailListActivity::class.java).apply {
            putParcelableArrayListExtra(
                SeriesDetailListActivity.KEY_BUNDLE_CLASSIFY_LIST_TAB,
                series.children as ArrayList<out Parcelable>
            )
            putExtra(
                SeriesDetailListActivity.KEY_BUNDLE_INIT_TAB_INDEX,
                series.children.indexOf(classify)
            )
        })
    }

    private fun tagSelectedChange(pos: Int) {
        viewDataBinding.seriesTagList.checkByPosition(pos)
    }
}