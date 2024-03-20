package com.example.whatsappclone.data.moldel

class UserAccount(
    val numberPhone: Long
)


class ChatBoxContacts(
    val user1: UserAccount,
    val user2 : UserAccount,
    val lastMessage: String
)
class Conversation(
    val user1: UserAccount,
    val user2 : UserAccount,
    val messages: List<Message> = emptyList()
)


private var _actualUser: Long = 0
val actualUser = _actualUser
fun actualUser(userLog: Long){
    _actualUser = userLog
}
