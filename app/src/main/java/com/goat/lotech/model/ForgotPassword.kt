package com.goat.lotech.model

import android.util.Log
import android.widget.Toast
import com.goat.lotech.ui.activity.ForgotPasswordActivity
import com.google.firebase.auth.FirebaseAuth

object ForgotPassword {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val TAG = ForgotPassword::class.java.simpleName
    var result: Boolean? = true

    fun forgotPassword(email: String, context: ForgotPasswordActivity) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                result =
                    if (it.isSuccessful) {
                        Log.d(TAG, "Konfirmasi berhasil: ")
                        true
                    } else {
                        Toast.makeText(
                            context,
                            "Konfirmasi gagal: " + it.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        false
                    }
            }
    }


}