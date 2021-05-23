package com.goat.lotech.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileModel (
    var uid: String? = null,
    var email: String? = null,
    var name: String? = null,
    var heightBody: String?= null,
    var weightBody: String? = null,
    var gender: String?= null,
    var image: String? = null,
    var birthDate: String? = null
        ) :Parcelable