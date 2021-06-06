package com.goat.lotech.ui.activity.marketplace.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarketplaceMainModel(
    var productId: String? = null,
    var productName: String? = null,
    var productDescription: String? = null,
    var productType: String? = null,
    var productDp: String? = null,
    var rating: Int? = 0,
    var merchantId: String? = null,
    var merchantName: String? = null,
    var price: Int? = null,
) : Parcelable