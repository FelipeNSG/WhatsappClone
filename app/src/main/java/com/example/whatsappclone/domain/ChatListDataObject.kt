package com.example.whatsappclone.domain

import com.example.whatsappclone.data.UserAccount
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ChatListDataObject(
    val userAccount: UserAccount,
    val message: Message,
    val userName: String = "USER",
    val userImage: String = "https://picsum.photos/id/230/200/300"
)

data class Message(
    val user: UserAccount,
    val content: String? ,
    val deliveryStatus: MessageDeliveryStatus = MessageDeliveryStatus.DELIVERED,
    val type: MessageType = MessageType.TEXT,
    val timeStamp: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("MMM dd yyyy")),
    val timeHour: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("HH:mm")),
    val unreadCount: Int?=null
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