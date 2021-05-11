package com.goat.lotech.model

import android.util.Log
import android.widget.Toast
import com.goat.lotech.ui.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth

object Login {

    private val auth = FirebaseAuth.getInstance()
    private val TAG = Login::class.java.simpleName
    var result: Boolean? = true

    fun login(email: String, password: String, context: LoginActivity) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    result =
                        if (auth.currentUser.isEmailVerified) {
                            Log.d(TAG, "signInWithEmail:success")
                            true
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                context, "Email belum anda verifikasi",
                                Toast.LENGTH_SHORT
                            ).show()
                            false
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        context, "Authentication failed: " + it.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    result = false
                }
            }
    }
}