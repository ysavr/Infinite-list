package com.savr.infinitelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.savr.infinitelist.model.Data
import kotlinx.android.synthetic.main.item_user.view.*
import java.lang.IllegalArgumentException

class DataAdapter (var list: ArrayList<Data>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        private const val TYPE_DATA = 0
        private const val TYPE_LOADING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_DATA -> {
                val view = inflater.inflate(R.layout.item_user, parent, false)
                DataViewHolder(view)
            }
            TYPE_LOADING -> {
                val view = inflater.inflate(R.layout.item_progress, parent, false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Different view type")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder) {
            holder.textTitle.text = list.get(position).title
            holder.textSubtitle.text = list.get(position).subtitle
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewType = list.get(position).category
        return when (viewType) {
            "data" -> TYPE_DATA
            else -> TYPE_LOADING
        }

    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textTitle = itemView.title
        var textSubtitle = itemView.subtitle

        init {
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "Click ${list.get(adapterPosition).title}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
