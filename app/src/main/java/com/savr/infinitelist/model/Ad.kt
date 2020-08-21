package com.savr.infinitelist.model

import com.google.gson.annotations.SerializedName

data class Ad(

	@field:SerializedName("company")
	val company: String,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("url")
	val url: String
)