package com.savr.infinitelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.savr.infinitelist.model.Data
import com.savr.infinitelist.remote.Api
import com.savr.infinitelist.remote.ApiClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var list: ArrayList<Data>
    private lateinit var adapter: DataAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var api : Api
    private lateinit var retrofit: Retrofit
    private var page = 1
    private var totalPage = 2
    private var isLoading = false
    private var notLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = ArrayList()
        adapter = DataAdapter(list)

        layoutManager = LinearLayoutManager(this)
        rvUsers.layoutManager = layoutManager
        rvUsers.adapter = adapter

        retrofit = ApiClient.getInstance()
        api = retrofit.create(Api::class.java)

        load(0)

        addScrollListener()
    }

    private fun addScrollListener() {
        rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (notLoading && layoutManager.findLastCompletelyVisibleItemPosition() == list.size -1) {
                    list.add(Data("progress"))
                    adapter.notifyItemInserted(list.size - 1)
                    notLoading = false

                    val call: Call<List<Data>>  = api.getData(list.size -1)
                    call.enqueue(object : Callback<List<Data>>{
                        override fun onFailure(call: Call<List<Data>>, t: Throwable) {

                        }

                        override fun onResponse(call: Call<List<Data>>?, response: Response<List<Data>>?
                        ) {
                            list.removeAt(list.size - 1)
                            adapter.notifyItemRemoved(list.size)
                            if (response!!.body()!!.isNotEmpty()) {
                                list.addAll(response.body()!!)
                                adapter.notifyDataSetChanged()
                                notLoading = true
                            } else {
                                Toast.makeText(applicationContext, "End of data",Toast.LENGTH_SHORT).show()
                            }

                        }

                    })
                }
            }
        })
    }

    fun load (page: Int) {
        val call: Call<List<Data>> = api.getData(page)
        call.enqueue(object : Callback<List<Data>>{
            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Log.e("failure",t.message.toString())
            }

            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful) {
                    list.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                }
            }

        })
    }
}
