package com.example.whatsappclone.screeens.chatScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.data.moldel.ChatBoxObject
import com.example.whatsappclone.data.moldel.ContactName
import com.example.whatsappclone.data.moldel.Message
import com.example.whatsappclone.data.moldel.UserAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ChatScreenViewModel(
    private val fireStoreManager: FireStoreManager,
    val numberContact: String,
    val userNameContact: String,
    val userLogPhoneAccount: String,
    private var idDocument: String
) : ViewModel() {

    suspend fun getImage(): String {
        var imageUrl: String = "esto es la variable"
        val chatList = getChat().first()
        if (chatList.isNotEmpty()) {
            if (chatList.first().userAccount1.numberPhone.toString() != userLogPhoneAccount) {
                imageUrl = chatList.first().userAccount1.userImage
            } else {
                imageUrl = chatList.first().userAccount2.userImage
            }
        }
        return imageUrl
    }


    fun getChat(): Flow<List<ChatBoxObject>> {
        return when(idDocument != "noIdDocument"){
            true -> {
                fireStoreManager.fetchChat(idDocument)
            }
            false -> {
                fireStoreManager.fetchChatWithoutId(numberContact, userLogPhoneAccount)
            }
        }
    }

    private fun getUsersAndCreateChat(
        userLogNumberPhone: String,
        userContactNumberPhone: String,
        addUserContactNumberPhone: String,
        contactName: String,
        contentMessage: String
    ) {

        fireStoreManager.fetchUserAccount(
            userLogNumberPhone,
            addUserContactNumberPhone
        ) { userList ->
            println("este es el el largo de la lista ${userList.size}")
            if (userList.size > 1) {
                viewModelScope.launch {
                    createChatBox(
                        userLogNumberPhone,
                        userContactNumberPhone,
                        userList,
                        contactName,
                        contentMessage
                    )
                }
            }
        }
    }

    private suspend fun createChatBox(
        userLog: String,
        contactNumberPhone: String,
        userList: List<UserAccount>,
        contactName: String,
        contentMessage: String
    ) {
        val user = userList.find { it.numberPhone.toString() == userLog }
        val newChatBox = ChatBoxObject(
            userAccount1 = userList[0],
            userAccount2 = userList[1],
            userNameChatContact = ContactName(name = contactName, numberPhone = contactNumberPhone),
            userNameChatUserLog = ContactName(name = userLog, numberPhone = userLog),
            messages = mutableListOf(
                Message(
                    user = user?.numberPhone.toString(),
                    content = contentMessage
                )
            )
        )
        fireStoreManager.createChatBox(newChatBox)

    }

    fun checkIfAChatAlreadyExists(
        userLog: String,
        userContactToAdd: String,
        userNameContactToAdd: String,
        contentMessage: String,

        callBack: (ChatScreenStated) -> Unit
    ) {
        var stated: ChatScreenStated = ChatScreenStated.Loading
        callBack(stated)
        fireStoreManager.consulterChat(userLog, userContactToAdd) { statedFireStore ->
            stated = when (statedFireStore) {
                FireStoreManager.FireStoreManagerState.Error -> {
                    ChatScreenStated.ErrorConnexion
                }

                FireStoreManager.FireStoreManagerState.Loading -> {
                    ChatScreenStated.Loading
                }

                FireStoreManager.FireStoreManagerState.ChatNotFound -> {
                    viewModelScope.launch {
                        getUsersAndCreateChat(
                            userLogNumberPhone = userLog,
                            userContactNumberPhone = userContactToAdd,
                            addUserContactNumberPhone = userContactToAdd,
                            contactName = userNameContactToAdd,
                            contentMessage = contentMessage
                        )
                    }
                    ChatScreenStated.NotFountChat
                }

                FireStoreManager.FireStoreManagerState.ChatFound -> {
                    ChatScreenStated.FoundChat
                }
            }
            callBack(stated)
        }
    }

    fun sendMessage(
        userPhoneAccount: String,
        numberContactToSendMessage: String,
        message: String
    ) {
        val messageToSend = Message(user = userPhoneAccount, content = message)
        fireStoreManager.sendMessageToChat(
            userPhoneAccount,
            numberContactToSendMessage,
            messageToSend
        )
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
    private val userPhoneAccount: String,
    private val idDocument: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ChatScreenViewModel::class.java)) {
            ChatScreenViewModel(
                fireStore,
                numberContactToAdd,
                userNameContactToAdd,
                userPhoneAccount,
                idDocument
            ) as T

        } else throw Exception("Error Factory")
    }
}