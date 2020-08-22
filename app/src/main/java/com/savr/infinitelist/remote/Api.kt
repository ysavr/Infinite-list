package com.savr.infinitelist.remote

import com.savr.infinitelist.model.Data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface Api {
    @GET("data.php")
    fun getData(@Query("index") index: Int) : Call<List<Data>>
}