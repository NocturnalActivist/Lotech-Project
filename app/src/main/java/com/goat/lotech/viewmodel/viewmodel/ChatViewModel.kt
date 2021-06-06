package com.goat.lotech.viewmodel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goat.lotech.model.ChatModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatViewModel : ViewModel() {

    private val chatList = MutableLiveData<ArrayList<ChatModel>>()
    private val TAG = ChatViewModel::class.java.simpleName

    fun setItemList(uid: String) {
        val listItem = ArrayList<ChatModel>()

        try {
            Firebase.firestore.collection("chat")
                .document(uid)
                .collection("listUser")
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        val model = ChatModel()
                        model.uid = document.data["uid"].toString()
                        model.name = document.data["name"].toString()
                        model.lastMessage = document.data["lastMessage"].toString()
                        model.lastMessageTime = document.data["lastMessageTime"].toString()
                        model.image = document.data["image"].toString()

                        listItem.add(model)
                    }
                    chatList.postValue(listItem)
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getChatList() : LiveData<ArrayList<ChatModel>> {
        return chatList
    }
}