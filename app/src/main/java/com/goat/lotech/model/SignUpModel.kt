package com.goat.lotech.model

import android.util.Log
import android.widget.Toast
import com.goat.lotech.ui.activity.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object SignUpModel {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val TAG = SignUpModel::class.java.simpleName
    var result = true

    fun register(email: String, password: String, name: String, context: SignUpActivity) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    saveDataToFirebase(email, name, context)
                } else {
                    Toast.makeText(context, "Registrasi gagal: " + it.exception?.message, Toast.LENGTH_SHORT).show()
                    result = false
                }
            }
    }

    private fun saveDataToFirebase(
        email: String,
        name: String,
        context: SignUpActivity
    ) {
        // create new user with their data
        val user = hashMapOf(
            "name" to name,
            "email" to email,
        )

        // add a new document with a generated ID
        Firebase.firestore.collection("users")
            .document(firebaseAuth.currentUser.uid)
            .set(user)
            .addOnSuccessListener {
                // send user email verification before login
                sendEmailVerification()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Terjadi masalah ketika proses pendaftaran: $it", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Terjadi masalah ketika proses pendaftaran: ", it)
                result = false
            }
    }

    private fun sendEmailVerification(): Boolean {
        firebaseAuth.currentUser.sendEmailVerification()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    result = true
                    return@addOnCompleteListener
                } else {
                    result = false
                    Log.d(TAG, "Email tidak terkirim: ${it.exception?.message}")
                }
            }
        return result
    }

}