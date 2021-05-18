package com.goat.lotech.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConsultantVerifyModel(
    var uid: String? = null,
    var name: String? = null,
    var description: String? = null,
    var sertifikatKeahlian: String? = null,
    var noHp: String? = null,
    var selfPhoto: String? = null,
    var ktp: String? = null,
    var sertifikat: String? = null,
    var like: Int? = 0,
    var price: String? = null,
) : Parcelable