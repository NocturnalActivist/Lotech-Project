package com.goat.lotech.storage

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.firebase.storage.FirebaseStorage

object ProfileManager {
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog

    private val TAG = ProfileManager::class.java.simpleName
    var image: String? = null

    fun uploadImageOption(it: FragmentActivity, imageUri: Uri) {
        mProgressDialog = ProgressDialog(it)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        val imageFileName = "users/image_${System.currentTimeMillis()}.png"
        val uploadTask = mStorageRef.child(imageFileName).putFile(imageUri)
        uploadTask.addOnSuccessListener {
            Log.e(TAG, "Image load successfully...")
            val downloadUriTask = mStorageRef.child(imageFileName).downloadUrl
            downloadUriTask.addOnSuccessListener { data ->
                image = data.toString()
                Log.e(TAG, "IMAGE PATH: $data")
                mProgressDialog.dismiss()

            }.addOnFailureListener {
                mProgressDialog.dismiss()
            }
        }.addOnFailureListener {
            Log.e(TAG, "Image upload failed ${it.printStackTrace()}")
            mProgressDialog.dismiss()
        }
    }

}