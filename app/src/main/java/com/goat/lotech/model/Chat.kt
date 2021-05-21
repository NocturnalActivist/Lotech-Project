package com.goat.lotech.model

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Chat {
    private val TAG = Chat::class.java.simpleName

    fun sendChat(
        message: String,
        name: String?,
        selfPhoto: String?,
        format: String,
        uid: String?,
        myName: String?,
        mySelfPhoto: String?,
        myUid: String
    ) {

        val lastUpdateMessageToSender = hashMapOf(
            "image" to selfPhoto,
            "lastMessage" to message,
            "lastMessageTime" to format,
            "name" to name,
            "uid" to uid,
        )

        val lastUpdateMessageToReceiver = hashMapOf(
            "image" to mySelfPhoto,
            "lastMessage" to message,
            "lastMessageTime" to format,
            "name" to myName,
            "uid" to myUid,
        )

        val logChat = hashMapOf(
            "isText" to false,
            "message" to message,
            "timestamp" to format,
            "uid" to myUid,
        )

        // receiver & sender update
        if (uid != null) {

            // update last message (sender)
            Firebase.firestore.collection("chat")
                .document(myUid)
                .collection("listUser")
                .document("${myUid}$uid")
                .set(lastUpdateMessageToSender)
                .addOnSuccessListener {
                    Log.d(TAG, "Success update last message")
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }

            // update last message (receiver)
            Firebase.firestore.collection("chat")
                .document(uid)
                .collection("listUser")
                .document("$uid${myUid}")
                .set(lastUpdateMessageToReceiver)
                .addOnSuccessListener {
                    Log.d(TAG, "Success update last message")
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }


            // save log chat (in sender side)
            Firebase.firestore.collection("chat")
                .document(myUid)
                .collection("listUser")
                .document("${myUid}$uid")
                .collection("logChat")
                .document(System.currentTimeMillis().toString())
                .set(logChat)
                .addOnSuccessListener {
                    Log.d(TAG, "Success send message")
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }

            // save log chat (in receiver side)
            Firebase.firestore.collection("chat")
                .document(uid)
                .collection("listUser")
                .document("$uid${myUid}")
                .collection("logChat")
                .document(System.currentTimeMillis().toString())
                .set(logChat)
                .addOnSuccessListener {
                    Log.d(TAG, "Success send message")
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }

        }
    }

}