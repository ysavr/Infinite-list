package com.savr.infinitelist.model

import com.google.gson.annotations.SerializedName

data class UsersResponse(

	@field:SerializedName("per_page")
	val perPage: Int,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("ad")
	val ad: Ad,

	@field:SerializedName("data")
	val data: ArrayList<Users>,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int
)