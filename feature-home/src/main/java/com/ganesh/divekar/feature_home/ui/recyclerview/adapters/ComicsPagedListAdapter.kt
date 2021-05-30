package com.ganesh.divekar.feature_home.ui.recyclerview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ganesh.divekar.feature_home.R
import com.ganesh.divekar.feature_home.databinding.ItemViewBinding
import com.ganesh.divekar.feature_home.ui.mapper.ComicsUIEntityMapper
import com.ganesh.divekar.feature_home.ui.recyclerview.viewholders.ComicsViewHolder
import com.ganesh.divekar.home.domain.entities.ComicsEntity


class ComicsPagedListAdapter(
    private val activity:FragmentActivity,
    private val comicsUIEntityMapper: ComicsUIEntityMapper
) :
    PagedListAdapter<ComicsEntity, ComicsViewHolder>(COMICS_COMPARATOR) {
    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemComicsBinding = DataBindingUtil.inflate<ItemViewBinding>(
            layoutInflater,
            R.layout.item_view,
            parent,
            false
        )
        return ComicsViewHolder(itemComicsBinding)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, comicsUIEntityMapper) }
      //  holder.binding.cardViewItemComics.animation=AnimationUtils.loadAnimation(activity,R.anim.fade_scale_up)
    }

    companion object {
        private val COMICS_COMPARATOR = object : DiffUtil.ItemCallback<ComicsEntity>() {
            override fun areItemsTheSame(oldItem: ComicsEntity, newItem: ComicsEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ComicsEntity, newItem: ComicsEntity): Boolean =
                oldItem == newItem
        }
    }
}
