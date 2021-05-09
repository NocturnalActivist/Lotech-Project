package com.goat.lotech.model

import android.util.Log
import android.widget.Toast
import com.goat.lotech.ui.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object LoginModel {

    private lateinit var auth: FirebaseAuth
    private val TAG = LoginModel::class.java.simpleName


    fun signIn(email: String, password: String, context:LoginActivity): Boolean {
        auth = Firebase.auth

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "Gagal Login: ", it.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
                return@addOnCompleteListener
            }
        return true
    }


}