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

    fun setUser(uid: String) {
        try {
            Firebase.firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener {
                        val userItem = ProfileModel()
                        userItem.email = it["email"].toString()
                        userItem.name = it["name"].toString()
                        userItem.heightBody = it["heightBody"].toString()
                        userItem.weightBody = it["weightBody"].toString()
                        userItem.image = it["image"].toString()
                        userItem.gender = it["gender"].toString()
                        userItem.birthDate = it["birthDate"].toString()

                        userProfile.postValue(userItem)

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