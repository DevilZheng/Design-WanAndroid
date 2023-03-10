package com.lowe.wanandroid.ui.navigator.child.tutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.DiffUtil
import com.lowe.common.base.http.adapter.getOrElse
import com.lowe.wanandroid.ui.ArticleDiffCalculator
import com.lowe.common.base.BaseViewModel
import com.lowe.wanandroid.ui.navigator.NavigatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TutorialChildViewModel @Inject constructor(private val repository: NavigatorRepository) :
    BaseViewModel() {

    val tutorialListLiveData: LiveData<Pair<List<Any>, DiffUtil.DiffResult>> = liveData {
        emit(
            getDiffResultPair(
                this.latestValue?.first ?: emptyList(),
                repository.getTutorialList().getOrElse { emptyList() }
            )
        )
    }

    private fun getDiffResultPair(oldList: List<Any>, newList: List<Any>) =
        newList to DiffUtil.calculateDiff(
            ArticleDiffCalculator.getCommonDiffCallback(
                oldList,
                newList
            )
        )
}