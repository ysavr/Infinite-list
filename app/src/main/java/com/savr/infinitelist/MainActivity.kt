package com.savr.infinitelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.savr.infinitelist.adapter.MyAdapter
import com.savr.infinitelist.interfaces.ILoadMore
import com.savr.infinitelist.model.Item
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ILoadMore {

    private lateinit var layoutManager: LinearLayoutManager
    private var items:MutableList<Item?> = ArrayList()
    lateinit var adapter:MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        random10Data()

        layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager
        adapter = MyAdapter(recycler, this, items)
        recycler.adapter = adapter
        adapter.setLoadedMore(this)
    }

    private fun random10Data() {
        for (i in 0..9) {
            val name = UUID.randomUUID().toString() + " - index $i"
            val item = Item(name, name.length)
            items.add(item)
        }
    }

    override fun onLoadMore() {
        if (items.size < 50) {
            items.add(null)
            adapter.notifyItemInserted(items.size - 1)

            Handler().postDelayed ({
                items.removeAt(items.size - 1)
                adapter.notifyItemRemoved(items.size - 1)

                val index = items.size
                val end = index+10

                for (i in index until end) {
                    val name = UUID.randomUUID().toString() + " - index $i"
                    val item = Item(name, name.length)
                    items.add(item)
                }

                adapter.notifyDataSetChanged()
                adapter.setLoaded()

            }, 3000)

        } else {
            Toast.makeText(applicationContext, "End of data", Toast.LENGTH_SHORT).show()
        }
    }

}
