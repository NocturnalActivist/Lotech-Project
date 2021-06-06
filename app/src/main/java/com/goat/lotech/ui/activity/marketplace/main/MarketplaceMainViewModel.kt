package com.goat.lotech.ui.activity.marketplace.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class MarketplaceMainViewModel : ViewModel() {

    private val productList = MutableLiveData<ArrayList<MarketplaceMainModel>>()
    private val TAG = MarketplaceMainModel::class.java.simpleName

    fun setAllItems(productType: String) {
        val listItem = ArrayList<MarketplaceMainModel>()

        try {
            Firebase.firestore.collection("product")
                .whereEqualTo("productType",productType)
                .get()
                .addOnSuccessListener { product ->
                    for(document in product) {
                        val model = MarketplaceMainModel()
                        model.productId = document.data["productId"].toString()
                        model.productName = document.data["productName"].toString()
                        model.productDp = document.data["productDp"].toString()
                        model.productDescription = document.data["productDescription"].toString()
                        model.merchantId = document.data["merchantId"].toString()
                        model.merchantName = document.data["merchantName"].toString()
                        model.price = document.data["price"].toString().toInt()
                        model.rating = document.data["rating"].toString().toInt()

                        listItem.add(model)
                    }
                    productList.postValue(listItem)
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getProductList() : LiveData<ArrayList<MarketplaceMainModel>> {
        return productList
    }

}