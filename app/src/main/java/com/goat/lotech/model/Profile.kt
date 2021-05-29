package com.goat.lotech.model

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Profile {

    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    fun updateUserData(updatedData: String, context: FragmentActivity?, child: String) {

        Firebase.firestore
            .collection("users")
            .document(uid)
            .update(child, updatedData)
            .addOnSuccessListener {
                Toast.makeText(context, "Data Anda Berhasil Diperbarui", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Gagal memperbarui data: ${it.printStackTrace()}", Toast.LENGTH_SHORT).show()
            }
    }

    fun updateImage(image: String?, context: FragmentActivity?) {
        Firebase.firestore
            .collection("users")
            .document(uid)
            .update("image", image)
            .addOnSuccessListener {
                Toast.makeText(context, "Data Anda Berhasil Diperbarui", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Gagal memperbarui data: ${it.printStackTrace()}", Toast.LENGTH_SHORT).show()
            }
    }
}