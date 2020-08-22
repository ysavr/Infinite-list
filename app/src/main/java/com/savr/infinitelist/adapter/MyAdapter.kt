package com.savr.infinitelist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.savr.infinitelist.MainActivity
import com.savr.infinitelist.R
import com.savr.infinitelist.interfaces.ILoadMore
import com.savr.infinitelist.model.Item
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.android.synthetic.main.item_loading.view.*

internal class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var progressBar = itemView.progress_bar
}

internal class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var text_name = itemView.txt_name
    var text_length = itemView.txt_length
}

class MyAdapter (recyclerView: RecyclerView, internal var activity: MainActivity, internal var items:MutableList<Item?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_ITEMTYPE = 0
    val VIEW_LOADINGTYPE = 1

    internal var loadMore:ILoadMore? = null
    internal var isLoading: Boolean = false
    internal var visibleThreshold = 5
    internal var lastVisibleItem: Int = 0
    internal var totalItemCount:Int = 0

    init {
        val linearLayout = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayout.itemCount
                lastVisibleItem = linearLayout.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (loadMore != null) {
                        loadMore!!.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_ITEMTYPE -> {
                val view = inflater.inflate(R.layout.item_layout, parent, false)
                ItemViewHolder(view)
            }
            VIEW_LOADINGTYPE -> {
                val view = inflater.inflate(R.layout.item_loading, parent, false)
                LoadingViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different view type")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.text_name.text = items[position]!!.name
            holder.text_length.text = items[position]!!.lenght.toString()

        } else if (holder is LoadingViewHolder) {
            holder.progressBar.isIndeterminate = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position]==null) VIEW_LOADINGTYPE else VIEW_ITEMTYPE
    }

    fun setLoaded() {
        isLoading = false
    }

    fun setLoadedMore(iLoadMore:ILoadMore) {
        this.loadMore = iLoadMore
    }
}