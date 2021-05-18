package com.goat.lotech.storage

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage

object ConsultAddManager {
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog

    private val TAG = ConsultAddManager::class.java.simpleName
    var selfPhoto: String? = null
    var ktp: String? = null
    var sertifikat: String? = null

    fun uploadImageOption(mContext: Context, imageURI: Uri, option: String) {
        mProgressDialog = ProgressDialog(mContext)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.show()
        val imageFileName = "consultant/$option/image_${System.currentTimeMillis()}.png"
        val uploadTask = mStorageRef.child(imageFileName).putFile(imageURI)
        uploadTask.addOnSuccessListener {
            Log.e(TAG, "Image load successfully...")
            val downloadUriTask = mStorageRef.child(imageFileName).downloadUrl
            downloadUriTask.addOnSuccessListener {
                when (option) {
                    "selfphoto" -> {
                        selfPhoto = it.toString()
                    }
                    "ktp" -> {
                        ktp = it.toString()
                    }
                    "sertifikat" -> {
                        sertifikat = it.toString()
                    }
                }

                Log.e(TAG, "IMAGE PATH: $it")
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