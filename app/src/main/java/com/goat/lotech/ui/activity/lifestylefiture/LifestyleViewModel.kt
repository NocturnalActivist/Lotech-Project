package com.goat.lotech.ui.activity.lifestylefiture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goat.lotech.model.Articles
import com.goat.lotech.network.ApiConfig
import com.goat.lotech.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LifestyleViewModel: ViewModel() {

    var listLifestyle: MutableLiveData<Articles> = MutableLiveData()
    private val errorMessage = MutableLiveData<String>()

    fun setLifestyle() {
        val apiConfig = ApiConfig.getApiService().create(ApiService::class.java)
        val call = apiConfig.getLifestyle()
        call.enqueue(object : Callback<Articles> {
            override fun onResponse(call: Call<Articles>, response: Response<Articles>) {
                    if(response.isSuccessful) {
                        listLifestyle.postValue(response.body())
                    } else {
                        listLifestyle.postValue(null)
                    }
            }

            override fun onFailure(call: Call<Articles>, t: Throwable) {
                listLifestyle.postValue(null)
                errorMessage.postValue(t.message)
            }

        })
    }

    fun getLifestyle() : MutableLiveData<Articles> {
        return listLifestyle
    }
}