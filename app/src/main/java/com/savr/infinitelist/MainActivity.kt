package com.savr.infinitelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.savr.infinitelist.model.Users
import com.savr.infinitelist.model.UsersResponse
import com.savr.infinitelist.remote.ApiClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: UsersAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var page = 1
    private var totalPage = 2
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        swipeRefresh.setOnRefreshListener(this)
        initView()
        getUsers(false)

        rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisible = layoutManager.findFirstVisibleItemPosition()
                val total = adapter.itemCount
                if (!isLoading && page < totalPage) {
                    if (visibleItemCount + pastVisible >= total) {
                        page++
                        getUsers(false)
                    }
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun getUsers(isOnRefresh: Boolean) {
        isLoading = true
        if (!isOnRefresh) progressBar.visibility = View.VISIBLE
        val params = HashMap<String, String>()
        params["page"] = page.toString()

        ApiClient.instance.getUsers(params).enqueue(object : Callback<UsersResponse>{
            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                totalPage = response.body()?.total!!
                val listUser = response.body()?.data
                if (listUser!=null) {
                    adapter.addList(listUser)
                }

                if (page == totalPage) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.INVISIBLE
                }
                isLoading = false
                swipeRefresh.isRefreshing = false
            }

        })
    }

    private fun initView() {
        rvUsers.setHasFixedSize(true)
        rvUsers.layoutManager = layoutManager
        adapter = UsersAdapter()
        rvUsers.adapter = adapter
    }

    override fun onRefresh() {
        adapter.clear()
        page = 1
        getUsers(true)
    }
}
