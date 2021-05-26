package com.goat.lotech.model

import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.goat.lotech.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

object Home {
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val TAG = Home::class.java.simpleName

    fun loadUserData(name: TextView, image: CircleImageView, activity: FragmentActivity?) {
        if (uid != null && activity != null) {
            Firebase.firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener {
                    name.visibility = View.VISIBLE
                    name.text = it["name"].toString()

                    Glide.with(activity)
                        .load(it["image"].toString())
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .error(R.drawable.ic_baseline_person_24)
                        .into(image)
                }
                .addOnFailureListener {
                    name.visibility = View.GONE
                    Toast.makeText(activity, "Gagal mendapatkan data pengguna", Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, it.message.toString())
                }
        }
    }


}