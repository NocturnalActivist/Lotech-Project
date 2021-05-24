package com.goat.lotech.model

data class ChatModel(
    var uid: String? = null,
    var name: String? = null,
    var lastMessage: String? = null,
    var lastMessageTime: String? = null,
    var image: String? = null,
)