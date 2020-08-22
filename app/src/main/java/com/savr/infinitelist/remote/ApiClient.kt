package com.savr.infinitelist.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        fun getInstance(): Retrofit {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://androidride.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit
        }
    }
}