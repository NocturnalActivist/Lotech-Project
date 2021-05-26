package com.goat.lotech.network

import com.goat.lotech.model.Articles
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("articles/all")
    fun getLifestyle() : Call<Articles>
}