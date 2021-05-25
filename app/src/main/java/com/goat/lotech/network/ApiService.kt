package com.goat.lotech.network

import com.goat.lotech.model.LifestyleResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("top-headlines?country=id&category=health&apiKey=2b5ef0c85da743d8a10c9d5c4b277672")
    fun getLifestyle() : Call<LifestyleResponse>
}