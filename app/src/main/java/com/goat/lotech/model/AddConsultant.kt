package com.goat.lotech.model

import android.util.Log
import android.widget.Toast
import com.goat.lotech.ui.activity.ConsultAddExpertActivity
import com.goat.lotech.ui.activity.ConsultVerifyDetailActivity
import com.goat.lotech.ui.activity.ConsultFindDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object AddConsultant {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val TAG = AddConsultant::class.java.simpleName
    var result: Boolean? = false

    fun addData(
        name: String,
        description: String,
        sertifikatKeahlian: String,
        noHp: String,
        selfPhoto: String?,
        ktp: String?,
        sertifikat: String?,
        context: ConsultAddExpertActivity
    ) {
        val user = hashMapOf(
            "name" to name,
            "description" to description,
            "sertifikatKeahlian" to sertifikatKeahlian,
            "phone" to noHp,
            "selfPhoto" to selfPhoto,
            "ktp" to ktp,
            "sertifikat" to sertifikat,
            "status" to "waiting",
            "price" to "0",
            "like" to 0,
        )

        // add a new document with a generated ID
        firebaseAuth.currentUser?.let {
            Firebase.firestore.collection("consultant")
                .document(it.uid)
                .set(user)
                .addOnSuccessListener {
                    result = true
                }
                .addOnFailureListener { fail ->
                    result = false
                    Toast.makeText(
                        context,
                        "Terjadi masalah ketika proses pendaftaran: $fail",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w(TAG, "Terjadi masalah ketika proses pendaftaran: ", fail)
                }
        }
    }

    fun verifyUser(uid: String, context: ConsultVerifyDetailActivity) {

        firebaseAuth.currentUser?.let {
            Firebase.firestore.collection("consultant")
                .document(uid)
                .update("status", "pakar")
                .addOnSuccessListener {
                    result = true
                }
                .addOnFailureListener { fail ->
                    result = false
                    Toast.makeText(
                        context,
                        "Terjadi masalah ketika proses verifikasi: $fail",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w(TAG, "Terjadi masalah ketika proses verifikasi: ", fail)
                }
        }
    }

    fun changePrice(
        updatedPrice: String,
        context: ConsultFindDetailActivity
    ) {
        firebaseAuth.currentUser?.uid?.let {
            Firebase.firestore.collection("consultant")
                .document(it)
                .update("price", updatedPrice)
                .addOnSuccessListener {
                    result = true
                }
                .addOnFailureListener { fail ->
                    result = false
                    Toast.makeText(
                        context,
                        "Terjadi masalah ketika proses verifikasi: $fail",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w(TAG, "Terjadi masalah ketika proses verifikasi: ", fail)
                }
        }
    }

    fun likedOrNot(option: Int?, uid: String?) {
        if (uid != null && option != null) {
            Firebase.firestore.collection("consultant")
                .document(uid)
                .update("like", option)
                .addOnSuccessListener {
                    Log.d(TAG, "Success")
                }
                .addOnFailureListener {
                    Log.e(TAG, "Fail: $it")
                }
        }
    }
}