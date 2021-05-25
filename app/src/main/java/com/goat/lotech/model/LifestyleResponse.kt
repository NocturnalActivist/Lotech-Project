package com.goat.lotech.model

import com.google.gson.annotations.SerializedName

data class LifestyleResponse(

	@field:SerializedName("totalResults")
	val totalResults: Int,

	@field:SerializedName("articles")
	val articles: List<ArticlesItem>,
)


