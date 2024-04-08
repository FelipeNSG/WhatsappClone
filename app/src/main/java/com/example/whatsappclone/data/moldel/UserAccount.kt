package com.example.whatsappclone.data.moldel

data class UserAccount(
    val numberPhone: Long = 0,
    val userImage: String = imageRandom()
)

class ChatBoxContacts(
    val chatOwner: UserAccount,
    val contact: UserAccount,
    val contactName:String,
    val lastMessage: String
)

class Conversation(
    val chatOwner: UserAccount,
    val contact: UserAccount,
    val contactName:String,
    val messages: List<Message> = emptyList()
)

fun imageRandom(): String {
    val number = (220..250).random()
    return "https://picsum.photos/id/$number/200/300"
}