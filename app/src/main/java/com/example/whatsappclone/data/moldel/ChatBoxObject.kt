package com.example.whatsappclone.data.moldel

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ContactName(
    val idUser: String = "Id user",
    val numberPhone: Long = 0,
    val userName: String = "User Name",
    val userAlias: String = "Alias",
    val userImage: String = "Uri_Image"
)

data class ChatBoxObject(
    var chatId: String = "",
    val messages: List<Message> = emptyList(),
    val dataUser1: ContactName = ContactName(),
    val dataUser2: ContactName = ContactName()
)

data class Message(
    val user: String = "",
    val uriImage: String= "",
    val content: String = "",
    val deliveryStatus: MessageDeliveryStatus = MessageDeliveryStatus.DELIVERED,
    val type: MessageType = MessageType.TEXT,
    val timeStamp: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("MMMM-dd-yyyy")),
    val timeHour: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("HH:mm")),
    val unreadCount: Int? = null
)


enum class MessageDeliveryStatus {
    DELIVERED,
    READ,
    PENDING,
    ERROR
}

enum class MessageType {
    TEXT,
    AUDIO,
    VIDEO,
    IMAGE
}

