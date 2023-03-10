package com.lowe.wanandroid.ui.project

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lowe.common.services.model.ProjectTitle
import com.lowe.wanandroid.ui.project.child.ProjectChildFragment

class ProjectChildFragmentAdapter(
    var items: List<ProjectTitle>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment {
        return ProjectChildFragment.newInstance(items[position].id)
    }
}