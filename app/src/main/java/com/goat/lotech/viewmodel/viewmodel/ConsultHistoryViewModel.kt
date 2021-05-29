package com.goat.lotech.viewmodel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goat.lotech.model.ConsultHistoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class ConsultHistoryViewModel : ViewModel() {

    private val listHistory = MutableLiveData<ArrayList<ConsultHistoryModel>>()
    private val TAG = ConsultHistoryViewModel::class.java.simpleName

    fun setListHistory(myUid: String, option: String) {
        val listItem = ArrayList<ConsultHistoryModel>()

        try {
            Firebase.firestore.collection("consult_history")
                .whereEqualTo(option, myUid)
                .whereNotEqualTo("pakarStatus",  "finish")
                //.whereNotEqualTo("userStatus", "finish")
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        val model = ConsultHistoryModel()
                        model.pakarUid = document.data["pakarUid"].toString()
                        model.timeInMillis = document.data["timeInMillis"].toString()
                        model.userStatus = document.data["userStatus"].toString()
                        model.pakarName = document.data["pakarName"].toString()
                        model.userName = document.data["userName"].toString()
                        model.pakarStatus = document.data["pakarStatus"].toString()
                        model.dateTime = document.data["dateTime"].toString()

                        listItem.add(model)
                    }
                    listHistory.postValue(listItem)
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun setListHistory2(myUid: String, option: String) {
        val listItem = ArrayList<ConsultHistoryModel>()

        try {
            Firebase.firestore.collection("consult_history")
                .whereEqualTo(option, myUid)
                .whereEqualTo("pakarStatus",  "finish")
                .whereEqualTo("userStatus", "finish")
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        val model = ConsultHistoryModel()
                        model.pakarName = document.data["pakarName"].toString()
                        model.userName = document.data["userName"].toString()
                        model.pakarStatus = document.data["pakarStatus"].toString()
                        model.dateTime = document.data["dateTime"].toString()
                        model.price = document.data["price"].toString()

                        listItem.add(model)
                    }
                    listHistory.postValue(listItem)
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }


    fun setListHistoryAll() {
        val listItem = ArrayList<ConsultHistoryModel>()

        try {
            Firebase.firestore.collection("consult_history")
                .whereEqualTo("bayarPakar", "waiting")
                .whereEqualTo("pakarStatus",  "finish")
                .whereEqualTo("userStatus", "finish")
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        val model = ConsultHistoryModel()
                        model.pakarName = document.data["pakarName"].toString()
                        model.userName = document.data["userName"].toString()
                        model.pakarStatus = document.data["pakarStatus"].toString()
                        model.dateTime = document.data["dateTime"].toString()
                        model.price = document.data["price"].toString()

                        listItem.add(model)
                    }
                    listHistory.postValue(listItem)
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }


    fun getListHistory() : LiveData<ArrayList<ConsultHistoryModel>> {
        return listHistory
    }




}