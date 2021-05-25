package com.goat.lotech.model

import com.google.gson.annotations.SerializedName

data class Source(

    @field:SerializedName("name")
    val name: String
)

data class ArticlesItem(

    @field:SerializedName("urlToImage")
    val urlToImage: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("source")
    val source: Source,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("url")
    val url: String
)