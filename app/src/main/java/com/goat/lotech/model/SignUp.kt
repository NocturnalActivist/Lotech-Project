package com.goat.lotech.model

import android.util.Log
import android.widget.Toast
import com.goat.lotech.ui.activity.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object SignUp {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val TAG = SignUp::class.java.simpleName
    var result: Boolean? = true

    fun register(
        email: String,
        password: String,
        name: String,
        heightBody: String,
        weightBody: String,
        gender: String?,
        birthDate: String,
        context: SignUpActivity,
    ) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    saveDataToFirebase(
                        email,
                        name,
                        heightBody,
                        weightBody,
                        gender,
                        birthDate,
                        context
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Registrasi gagal: " + it.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    result = false
                }
            }
    }

    private fun saveDataToFirebase(
        email: String,
        name: String,
        heightBody: String,
        weightBody: String,
        gender: String?,
        birthDate: String,
        context: SignUpActivity
    ) {
        // create new user with their data
        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "heightBody" to heightBody,
            "weightBody" to weightBody,
            "gender" to gender,
            "birthDate" to birthDate,
            "image" to "",
            "role" to "user",
        )

        // add a new document with a generated ID
        firebaseAuth.currentUser?.let {
            Firebase.firestore.collection("users")
                .document(it.uid)
                .set(user)
                .addOnSuccessListener {
                    // send user email verification before login
                    sendEmailVerification()
                }
                .addOnFailureListener { fail ->
                    Toast.makeText(
                        context,
                        "Terjadi masalah ketika proses pendaftaran: $fail",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w(TAG, "Terjadi masalah ketika proses pendaftaran: ", fail)
                    result = false
                }
        }
    }

    private fun sendEmailVerification() {
        firebaseAuth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "Sukses mendaftar")
                    result = true
                } else {
                    result = false
                    Log.d(TAG, "Email tidak terkirim: ${it.exception?.message}")
                }
            }
    }

}