package com.goat.lotech.viewmodel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goat.lotech.model.ProfileModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileViewModel: ViewModel() {

    private val userProfile = MutableLiveData<ProfileModel>()
    private val TAG = ProfileViewModel::class.java.simpleName

    fun setUser() {
        try {
            Firebase.firestore.collection("users")
//                .whereEqualTo("role", "user")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val userItem = ProfileModel()
                        userItem.uid = document.id
                        userItem.email = document.data["email"].toString()
                        userItem.name = document.data["name"].toString()
                        userItem.heightBody = document.data["heightBody"].toString()
                        userItem.weightBody = document.data["weightBody"].toString()
                        userItem.image = document.data["image"].toString()
                        userItem.gender = document.data["gender"].toString()
                        userItem.birthDate = document.data["birthDate"].toString()

                        userProfile.postValue(userItem)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getUser() : LiveData<ProfileModel> {
        return userProfile
    }
}