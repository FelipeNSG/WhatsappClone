package com.example.whatsappclone.data

import com.example.whatsappclone.data.moldel.ChatBoxObject
import com.example.whatsappclone.data.moldel.UserAccount
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class FireStoreManager {

    fun userExist(callBack: () -> Unit): Boolean {
        return true
    }

    private val fireStore = FirebaseFirestore.getInstance()
    suspend fun createUser(user: UserAccount) {
        fireStore.collection("users").add(user).await()
    }

    suspend fun createChatBox(chatBox: ChatBoxObject) {
        fireStore.collection("chats").add(chatBox).await()
    }


    fun fetchUser(numberPhone: String, callBack: (FireStoreManagerState) -> Unit) {
        var stated: FireStoreManagerState = FireStoreManagerState.Loading
        callBack(stated)
        try {
            fireStore.collection("users")
                .whereEqualTo("numberPhone", numberPhone.toLong())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null) {
                            stated = if (document.isEmpty) {
                                FireStoreManagerState.NoSuccess
                            } else {
                                FireStoreManagerState.Success
                            }
                        }
                        callBack(stated)
                    }
                }
        } catch (ex: Exception) {
            ex.printStackTrace()
            FireStoreManagerState.Error
            callBack(stated)
        }
    }

    fun fetchChats(
        user1: String,
        user2: String,
    ){

    }

    fun consulterChats(
        user1: String,
        user2: String,
        callBack: (FireStoreManagerState) -> Unit
    ) {
        var stated: FireStoreManagerState = FireStoreManagerState.Loading
        callBack(stated)
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
                        println("Se encontró un documento: ${document.data}")
                    }
                }
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        stated = if (document.isEmpty) {
                            println("No Se encontro el documento")
                            FireStoreManagerState.NoSuccess
                        } else {
                            println("sii Se encontro el documento")
                            FireStoreManagerState.Success
                        }
                    }
                    callBack(stated)
                }
            }
            .addOnFailureListener { exception ->
                println("Error de red $exception")
                stated = FireStoreManagerState.Error
                callBack(stated)
            }
    }

    fun getNumberPhone(userConversation: Long): Flow<List<UserAccount>> = callbackFlow {
        val usersRef = fireStore.collection("users")
            .whereEqualTo("numberPhone", userConversation)

        val subscription = usersRef.addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                val users = mutableListOf<UserAccount>()
                for (document in snapshot.documents) {
                    val user = document.toObject(UserAccount::class.java)
                    user?.let { users.add(it) }
                }
                trySend(users).isSuccess
            }
        }
        awaitClose {
            subscription.remove()
        }
    }

    sealed class FireStoreManagerState {
        data object Loading : FireStoreManagerState()
        data object Error : FireStoreManagerState()

        data object Success : FireStoreManagerState()

        data object NoSuccess : FireStoreManagerState()
    }

}

