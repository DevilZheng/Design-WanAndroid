package com.lowe.wanandroid.ui.project.child.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lowe.common.services.model.Article
import com.lowe.multitype.PagingItemViewBinder
import com.lowe.wanandroid.R
import com.lowe.wanandroid.databinding.ItemProjectArticleLayoutBinding
import com.lowe.wanandroid.ui.ViewBindingHolder
import com.lowe.wanandroid.ui.home.item.ArticleAction

class ProjectChildItemBinder(private val onClick: (ArticleAction) -> Unit) :
    PagingItemViewBinder<Article, ViewBindingHolder<ItemProjectArticleLayoutBinding>>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBindingHolder<ItemProjectArticleLayoutBinding> {
        return ViewBindingHolder(
            DataBindingUtil.inflate(
                inflater,
                R.layout.item_project_article_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewBindingHolder<ItemProjectArticleLayoutBinding>,
        item: Article
    ) {
        holder.binding.apply {
            article = item
            root.setOnClickListener {
                onClick(
                    ArticleAction.ItemClick(
                        holder.bindingAdapterPosition,
                        item
                    )
                )
            }
            projectCollect.setOnClickListener {
                onClick(
                    ArticleAction.CollectClick(
                        holder.bindingAdapterPosition,
                        item
                    )
                )
            }
            executePendingBindings()
        }
    }
}