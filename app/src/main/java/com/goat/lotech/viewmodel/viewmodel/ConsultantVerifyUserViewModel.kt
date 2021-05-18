package com.goat.lotech.viewmodel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goat.lotech.model.ConsultantVerifyModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConsultantVerifyUserViewModel : ViewModel() {
    private val consultantList = MutableLiveData<ArrayList<ConsultantVerifyModel>>()
    private val TAG = ConsultantVerifyUserViewModel::class.java.simpleName

    fun setUser() {
        val listItems = ArrayList<ConsultantVerifyModel>()


        try {
            Firebase.firestore.collection("consultant")
                .whereEqualTo("status", "waiting")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val userItem = ConsultantVerifyModel()
                        userItem.uid = document.id
                        userItem.name = document.data["name"].toString()
                        userItem.description = document.data["description"].toString()
                        userItem.noHp = document.data["phone"].toString()
                        userItem.selfPhoto = document.data["selfPhoto"].toString()
                        userItem.sertifikatKeahlian = document.data["sertifikatKeahlian"].toString()
                        userItem.ktp = document.data["ktp"].toString()
                        userItem.sertifikat = document.data["sertifikat"].toString()

                        listItems.add(userItem)
                    }
                    consultantList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }
    
    fun getUser() : LiveData<ArrayList<ConsultantVerifyModel>> {
        return consultantList
    }
}