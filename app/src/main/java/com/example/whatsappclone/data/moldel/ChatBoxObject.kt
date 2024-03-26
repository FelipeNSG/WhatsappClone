package com.example.whatsappclone.data.moldel

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ChatBoxObject(
    val userAccount1: UserAccount,
    val userAccount2: UserAccount,
    val messages: List<Message> = listOf(),
    val userName: String,
)

data class Message(
    val user: String,
    val content: String?,
    val deliveryStatus: MessageDeliveryStatus = MessageDeliveryStatus.DELIVERED,
    val type: MessageType = MessageType.TEXT,
    val timeStamp: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("MMMM-dd-yyyy")),
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

