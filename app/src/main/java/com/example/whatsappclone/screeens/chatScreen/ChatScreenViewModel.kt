package com.example.whatsappclone.screeens.chatScreen

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.data.FirebaseStorageManager
import com.example.whatsappclone.data.Response
import com.example.whatsappclone.data.moldel.ChatBoxObject
import com.example.whatsappclone.data.moldel.ContactName
import com.example.whatsappclone.data.moldel.Message
import com.example.whatsappclone.data.moldel.MessageType
import com.example.whatsappclone.data.moldel.UserAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ChatScreenViewModel(
    private val fireStoreManager: FireStoreManager,
    private val storage: FirebaseStorageManager,
    val numberContact: String,
    val userAlias: String,
    val userLogPhoneAccount: String,
    private var idDocument: String
) : ViewModel() {

    var addImageToStorageResponse by mutableStateOf<Response<Uri>>(Response.Success(null))
    fun addImageToStorage(imageUri: Uri) = viewModelScope.launch {
        addImageToStorageResponse = Response.Loading
        addImageToStorageResponse = storage.addImageToFirebaseStorage(imageUri).also {
            when(it){
                is Response.Failure -> {
                    println("Error al enviar una imagen a storage")
                }
                Response.Loading -> {Unit}
                is Response.Success -> {
                    sendMessage(
                        userPhoneAccount = userLogPhoneAccount,
                        numberContactToSendMessage = numberContact,
                        message = Message(
                            content = "Esto es una imagen de la galerria",
                            uriImage = it.data.toString(),
                            type = MessageType.IMAGE
                        )
                    )
                }
            }

        }
    }

    suspend fun getImage(): String {
        var imageUrl = "This is a URL"
        val chatList = getChat().first()
        if (chatList.isNotEmpty()) {
            imageUrl = if (chatList.first().dataUser1.numberPhone != userLogPhoneAccount.toLong()) {
                chatList.first().dataUser2.userImage
            } else {
                chatList.first().dataUser1.userImage
            }
        }
        return imageUrl
    }

    fun getChat(): Flow<List<ChatBoxObject>> {
        return when (idDocument != "noIdDocument") {
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
        userAlias: String,
        contentMessage: Message
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
                        userAlias,
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
        userContactAlias: String,
        contentMessage: Message
    ) {
        val user = userList.find { it.numberPhone.toString() == userLog }
        val newChatBox = ChatBoxObject(
            dataUser2 = ContactName(userAlias = userContactAlias, numberPhone = contactNumberPhone.toLong(), userImage = userList[1].userImage),
            dataUser1 = ContactName(userAlias = userLog, numberPhone = userLog.toLong(), userImage = userList[0].userImage),
            messages = mutableListOf(
                contentMessage
            )
        )
        fireStoreManager.createChatBox(newChatBox)
    }

    fun checkIfAChatAlreadyExists(
        userLog: String,
        userContactToAdd: String,
        userNameContactToAdd: String,
        contentMessage: Message,

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
                            userAlias = userNameContactToAdd,
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
        message: Message
    ) {
        if (message.content.isNotBlank()) {
            fireStoreManager.sendMessageToChat(
                userPhoneAccount,
                numberContactToSendMessage,
                message
            )
        }
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
    private val storage: FirebaseStorageManager,
    private val numberContactToAdd: String,
    private val userNameContactToAdd: String,
    private val userPhoneAccount: String,
    private val idDocument: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ChatScreenViewModel::class.java)) {
            ChatScreenViewModel(
                fireStore,
                storage,
                numberContactToAdd,
                userNameContactToAdd,
                userPhoneAccount,
                idDocument
            ) as T

        } else throw Exception("Error Factory")
    }
}