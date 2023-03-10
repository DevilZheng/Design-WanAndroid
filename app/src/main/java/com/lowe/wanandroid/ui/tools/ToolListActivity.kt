package com.lowe.wanandroid.ui.tools

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lowe.multitype.MultiTypeAdapter
import com.lowe.wanandroid.R
import com.lowe.wanandroid.databinding.ActivityToolsBinding
import com.lowe.common.services.model.ToolBean
import com.lowe.wanandroid.ui.ActivityDataBindingDelegate
import com.lowe.wanandroid.BaseActivity
import com.lowe.wanandroid.ui.web.WebActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToolListActivity : BaseActivity<ToolListViewModel, ActivityToolsBinding>() {

    private val toolAdapter = MultiTypeAdapter().apply {
        register(ToolItemBinder(this@ToolListActivity::onToolClick))
    }

    override val viewDataBinding: ActivityToolsBinding by ActivityDataBindingDelegate(R.layout.activity_tools)

    override val viewModel: ToolListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvents()
    }

    private fun initView() {
        viewDataBinding.apply {
            with(toolList) {
                adapter = toolAdapter
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
            with(toolbar) {
                setNavigationOnClickListener { finish() }
            }
        }
    }

    private fun initEvents() {
        viewModel.toolsLiveData.observe(this) {
            toolAdapter.items = it
            toolAdapter.notifyDataSetChanged()
        }
    }

    private fun onToolClick(position: Int, data: ToolBean) {
        WebActivity.loadUrl(this, data.link)
    }
}