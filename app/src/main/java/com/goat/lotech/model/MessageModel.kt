package com.goat.lotech.model

data class MessageModel(
    var message: String? = null,
    var time: String? = null,
    var uid: String? = null,
    var isText: Boolean? = false,
)