package com.example.whatsappclone.screeens.chatScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.data.moldel.ChatBoxObject
import com.example.whatsappclone.data.moldel.Message
import com.example.whatsappclone.data.moldel.UserAccount


class ChatScreenViewModel(
    private val fireStoreManager: FireStoreManager,
    val numberContactToAdd: String,
    val userNameContactToAdd: String,
    val userPhoneAccount: String
) : ViewModel() {


    val chatBoxList = mutableListOf<ChatBoxObject>()
    suspend fun createChatBox(
        userLog: String,
        contact: String,
        contactName: String,
        contentMessage: String
    ) {
        val newChatBox = ChatBoxObject(
            userAccount1 = UserAccount(userLog.toLong()),
            userAccount2 = UserAccount(contact.toLong()),
            userName = contactName,
            messages = mutableListOf(
                Message(
                    user = userLog,
                    content = contentMessage
                )
            )
        )
        fireStoreManager.createChatBox(newChatBox)
    }

    fun getChatsBox(
        user1: String,
        user2: String,
        callBack: (ChatScreenStated) -> Unit
    ) {
        var stated: ChatScreenStated = ChatScreenStated.Loading
        callBack(stated)
        fireStoreManager.fetchConsulterChats(user1, user2) { statedFireStore ->
           stated = when (statedFireStore) {
                FireStoreManager.FireStoreManagerState.Error -> {
                    ChatScreenStated.ErrorConnexion
                }

                FireStoreManager.FireStoreManagerState.Loading -> {
                    ChatScreenStated.Loading
                }

                FireStoreManager.FireStoreManagerState.NoSuccess -> {
                    ChatScreenStated.NotFountChat
                }

                FireStoreManager.FireStoreManagerState.Success -> {
                    ChatScreenStated.FoundChat
                }
            }
            callBack(stated)
        }
    }

    fun checkIfAChatAlreadyExists() {

    }

    fun screenActualChat() {

    }

    sealed class ChatScreenStated {
        data object Loading : ChatScreenStated()
        data object FoundChat : ChatScreenStated()
        data object NotFountChat : ChatScreenStated()
        data object ErrorConnexion : ChatScreenStated()
    }

}

class MyViewModelFactoryChatScreen(
    private val fireStore: FireStoreManager,
    private val numberContactToAdd: String,
    private val userNameContactToAdd: String,
    private val userPhoneAccount: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ChatScreenViewModel::class.java)) {
            ChatScreenViewModel(
                fireStore,
                numberContactToAdd,
                userNameContactToAdd,
                userPhoneAccount
            ) as T

        } else throw Exception("Error Factory")
    }
}