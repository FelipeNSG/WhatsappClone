package com.example.whatsappclone.data

import com.example.whatsappclone.data.moldel.ChatBoxObject
import com.example.whatsappclone.data.moldel.Message
import com.example.whatsappclone.data.moldel.UserAccount
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class FireStoreManager {
    private val fireStore = FirebaseFirestore.getInstance()

//These are the functions of Firestore
     fun createUser(user: UserAccount, onSuccess: () -> Unit) {
        fireStore.collection("users").add(user).isSuccessful.apply { onSuccess() }
    }

    suspend fun createChatBox(chatBox: ChatBoxObject) {
        fireStore.collection("chats").add(chatBox).await()
    }

    fun fetchUserAccount(
        numberPhone1: String,
        numberPhone2: String,
        callBack: (List<UserAccount>) -> Unit
    ) {
        val listUserAccount = mutableListOf<UserAccount>()

        fireStore.collection("users")
            .where(
                Filter.or(
                    Filter.equalTo("numberPhone", (numberPhone1.toLong())),
                    Filter.equalTo("numberPhone", (numberPhone2.toLong()))
                )
            )
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.exists()) {
                        listUserAccount.add(document.toObject<UserAccount>())
                        document.toObject<UserAccount>()
                    }
                    callBack(listUserAccount)
                }
            }
            .addOnFailureListener { exception ->
                println("Error de red $exception")

            }
    }

    fun fetchIfUserExist(
        numberPhone: String,
        callBack: (FireStoreManagerUserConsultState) -> Unit
    ) {
        var stated: FireStoreManagerUserConsultState = FireStoreManagerUserConsultState.Error
        /*callBack(stated)*/
        try {
            fireStore.collection("users")
                .whereEqualTo("numberPhone", numberPhone.toLong())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null) {
                            stated = if (document.isEmpty) {
                                FireStoreManagerUserConsultState.UserNotFound
                            } else {
                                FireStoreManagerUserConsultState.UserFound
                            }
                        }
                    } else {
                        stated = FireStoreManagerUserConsultState.Error
                    }
                    callBack(stated)
                }
        } catch (ex: Exception) {
            ex.printStackTrace()
            FireStoreManagerState.Error
            callBack(stated)
        }
    }

    fun consulterChat(
        user1: String,
        user2: String,
        callBack: (FireStoreManagerState) -> Unit
    ) {
        var stated: FireStoreManagerState = FireStoreManagerState.Loading
        val docRef = fireStore.collection("chats")
        docRef.where(
            Filter.and(
                Filter.or(
                    Filter.equalTo("userAccount1.numberPhone", (user1.toLong())),
                    Filter.equalTo("userAccount1.numberPhone", (user2.toLong()))
                ),
                Filter.or(
                    Filter.equalTo("userAccount2.numberPhone", (user1.toLong())),
                    Filter.equalTo("userAccount2.numberPhone", (user2.toLong()))
                )
            )
        )
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        stated = if (document.isEmpty) {
                            println("No Se encontro el documento del chat")
                            FireStoreManagerState.ChatNotFound
                        } else {
                            println("sii Se encontro el documento del chat")

                            FireStoreManagerState.ChatFound
                        }
                        callBack(stated)
                    }
                }else{
                    stated = FireStoreManagerState.Error
                }
            }
            .addOnFailureListener { exception ->
                println("Error de red $exception")
                stated = FireStoreManagerState.Error
                callBack(stated)
            }
    }

    fun sendMessageToChat(
        user1: String,
        user2: String,
        message: Message
    ) {
        val docRef = fireStore.collection("chats")
        docRef.where(
            Filter.and(
                Filter.or(
                    Filter.equalTo("userAccount1.numberPhone", (user1.toLong())),
                    Filter.equalTo("userAccount1.numberPhone", (user2.toLong()))
                ),
                Filter.or(
                    Filter.equalTo("userAccount2.numberPhone", (user1.toLong())),
                    Filter.equalTo("userAccount2.numberPhone", (user2.toLong()))
                )
            )
        )

            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.exists()) {
                        println("serealizo la buusqueda correcta")
                        document.reference.update("messages", FieldValue.arrayUnion(message))
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error de red $exception")
            }
    }


    fun fetchChat(
        documentId: String,
    ): Flow<List<ChatBoxObject>> = callbackFlow {
        val chatRef = fireStore.collection("chats").document(documentId)

        val subscription = chatRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Listen failed: $e")
                return@addSnapshotListener
            }
            snapshot?.let { querySnapshot ->
                val chatList = mutableListOf<ChatBoxObject>()
                if (querySnapshot.exists()) {
                    try {
                        val chatBoxItem = querySnapshot.toObject<ChatBoxObject>()
                        chatBoxItem?.let { chatList.add(it) }
                    } catch (ex: Exception) {
                        println("Error to convert document to ChatBoxObject $ex")
                    }
                }

                trySend(chatList).isSuccess
            }
        }
        awaitClose { subscription.remove() }

    }

    fun fetchChatWithoutId(
        user1: String,
        user2: String,
    ): Flow<List<ChatBoxObject>> = callbackFlow {
        val chatRef = fireStore.collection("chats")
            .where(
                Filter.and(
                    Filter.or(
                        Filter.equalTo("userAccount1.numberPhone", (user1.toLong())),
                        Filter.equalTo("userAccount1.numberPhone", (user2.toLong()))
                    ),
                    Filter.or(
                        Filter.equalTo("userAccount2.numberPhone", (user1.toLong())),
                        Filter.equalTo("userAccount2.numberPhone", (user2.toLong()))
                    )
                )
            )
        val subscription = chatRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Listen failed: $e")
                return@addSnapshotListener
            }
            val chatList = mutableListOf<ChatBoxObject>()
            snapshot?.let { querySnapshot ->
                for (document in querySnapshot.documents) {
                    if (document.exists()) {
                        try {
                            val chatBoxItem = document.toObject<ChatBoxObject>()
                            chatBoxItem?.iD = document.id
                            chatBoxItem?.let { chatList.add(it) }
                        } catch (ex: Exception) {
                            println("Error to convert document to ChatBoxObject $ex")
                        }
                    }
                }
                trySend(chatList).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }


    fun getListChatBox(numberPhone: String): Flow<List<ChatBoxObject>> = callbackFlow {
        val chatsRef = fireStore.collection("chats")
            .where(
                Filter.or(
                    Filter.equalTo("userAccount1.numberPhone", (numberPhone.toLong())),
                    Filter.equalTo("userAccount2.numberPhone", (numberPhone.toLong()))
                )
            )
        val subscription = chatsRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Listen failed: $e")
                return@addSnapshotListener
            }
            snapshot?.let { querySnapshot ->
                val chatList = mutableListOf<ChatBoxObject>()
                for (document in querySnapshot.documents) {
                    if (document.exists()) {
                        try {
                            val chatBoxItem = document.toObject<ChatBoxObject>()
                            chatBoxItem?.iD = document.id
                            chatBoxItem?.let { chatList.add(it) }
                        } catch (ex: Exception) {
                            println("Error to convert document to ChatBoxObject $ex")
                        }
                    }
                }
                trySend(chatList).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

    sealed class FireStoreManagerState {
        data object Loading : FireStoreManagerState()
        data object Error : FireStoreManagerState()

        data object ChatFound : FireStoreManagerState()

        data object ChatNotFound : FireStoreManagerState()
    }

    sealed class FireStoreManagerUserConsultState {
        data object Loading : FireStoreManagerUserConsultState()
        data object Error : FireStoreManagerUserConsultState()

        data object UserFound : FireStoreManagerUserConsultState()

        data object UserNotFound : FireStoreManagerUserConsultState()
    }

}

