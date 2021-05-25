package com.goat.lotech.ui.activity.lifestylefiture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goat.lotech.model.LifestyleResponse
import com.goat.lotech.network.ApiConfig
import com.goat.lotech.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LifestyleViewModel: ViewModel() {

    var listLifestyle: MutableLiveData<LifestyleResponse> = MutableLiveData()

    fun setLifestyle() {
        val apiConfig = ApiConfig.getApiService().create(ApiService::class.java)
        val call = apiConfig.getLifestyle()
        call.enqueue(object : Callback<LifestyleResponse> {
            override fun onResponse(call: Call<LifestyleResponse>, response: Response<LifestyleResponse>) {
                if(response.isSuccessful) {
                    listLifestyle.postValue(response.body())
                } else {
                    listLifestyle.postValue(null)
                }
            }

            override fun onFailure(call: Call<LifestyleResponse>, t: Throwable) {
                listLifestyle.postValue(null)
            }
        })
    }

    fun getLifestyle() : MutableLiveData<LifestyleResponse> {
        return listLifestyle
    }
}