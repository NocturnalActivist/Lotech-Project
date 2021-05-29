package com.goat.lotech.model

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.goat.lotech.ui.activity.ConsultAddExpertActivity
import com.goat.lotech.ui.activity.ConsultVerifyDetailActivity
import com.goat.lotech.ui.activity.ConsultFindDetailActivity
import com.goat.lotech.ui.activity.ConsultPaymentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object AddConsultant {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val TAG = AddConsultant::class.java.simpleName
    var result: Boolean? = false
    lateinit var mProgressDialog: ProgressDialog

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

    fun makeFreeConsult(
        context: ConsultFindDetailActivity,
        dateTime: String,
        pakarUid: String?,
        userUid: String,
        userName: String,
        pakarName: String?,
        timeInMillis: String
    ) {
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()

        val consultHistory = hashMapOf(
            "userUid" to userUid,
            "pakarUid" to pakarUid,
            "userName" to userName,
            "pakarName" to pakarName,
            "userStatus" to "Siap",
            "pakarStatus" to "Menunggu persetujuan",
            "dateTime" to dateTime,
            "bukti" to "null",
            "timeInMillis" to timeInMillis,
            "price" to "0",
            "bayarPakar" to "done",
        )

        Firebase.firestore
            .collection("consult_history")
            .document(
                timeInMillis
            )
            .set(consultHistory)
            .addOnSuccessListener {
                mProgressDialog.dismiss()
                Toast.makeText(
                    context,
                    "Sukses melakukan transaksi, silahkan menunggu ketersediaan pakar untuk melaksanakan konsultasi",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                mProgressDialog.dismiss()
                it.printStackTrace()
            }
    }

    fun uploadBuktiTransfer(
        image: String?,
        context: ConsultPaymentActivity,
        format: String,
        myUid: String,
        pakarName: String,
        pakarUid: String,
        timeInMillis: String,
        price: String
    ) {
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()

        Firebase.firestore.collection("users")
            .document(myUid)
            .get()
            .addOnSuccessListener {

                val userName = it["name"].toString()

                val consultHistory = hashMapOf(
                    "userUid" to myUid,
                    "pakarUid" to pakarUid,
                    "userName" to userName,
                    "pakarName" to pakarName,
                    "userStatus" to "Siap",
                    "pakarStatus" to "Menunggu persetujuan",
                    "dateTime" to format,
                    "bukti" to image,
                    "timeInMillis" to timeInMillis,
                    "price" to price,
                    "bayarPakar" to "waiting",
                )

                Firebase.firestore
                    .collection("consult_history")
                    .document(System.currentTimeMillis().toString())
                    .set(consultHistory)
                    .addOnSuccessListener {
                        mProgressDialog.dismiss()
                        Toast.makeText(
                            context,
                            "Sukses melakukan transaksi, silahkan menunggu ketersediaan pakar untuk melaksanakan konsultasi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { data ->
                        mProgressDialog.dismiss()
                        data.printStackTrace()
                    }
            }
            .addOnFailureListener {
                mProgressDialog.dismiss()
                it.printStackTrace()
            }
    }

    fun pakarReady(context: Context, timeInMillis: String) {
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        Firebase.firestore
            .collection("consult_history")
            .document(timeInMillis)
            .update("pakarStatus", "Siap")
            .addOnSuccessListener {
                mProgressDialog.dismiss()
                Toast.makeText(
                    context,
                    "Sukses melakukan transaksi, silahkan menunggu ketersediaan pakar untuk melaksanakan konsultasi",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                mProgressDialog.dismiss()
                it.printStackTrace()
            }
    }

    fun finishConsult(context: Context?, timeInMillis: String, role: String) {
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        Firebase.firestore
            .collection("consult_history")
            .document(timeInMillis)
            .update(role, "finish")
            .addOnSuccessListener {
                mProgressDialog.dismiss()
                Toast.makeText(
                    context,
                    "Sukses melakukan transaksi, silahkan menunggu ketersediaan pakar untuk melaksanakan konsultasi",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                mProgressDialog.dismiss()
                it.printStackTrace()
            }
    }

    fun bayarPakar(context: Context?, timeInMillis: String) {
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        Firebase.firestore
            .collection("consult_history")
            .document(timeInMillis)
            .update("bayarPakar", "done")
            .addOnSuccessListener {
                mProgressDialog.dismiss()
                Toast.makeText(
                    context,
                    "Sukses melakukan transaksi, silahkan menunggu ketersediaan pakar untuk melaksanakan konsultasi",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                mProgressDialog.dismiss()
                it.printStackTrace()
            }
    }
}