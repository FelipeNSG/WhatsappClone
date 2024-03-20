package com.example.whatsappclone.data

import com.example.whatsappclone.data.moldel.Conversation
import com.example.whatsappclone.data.moldel.UserAccount
import com.example.whatsappclone.screeens.homeScreen.CallbackNavControllerNavigationToChatScreen
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class FireStoreManager {

    private val fireStore = FirebaseFirestore.getInstance()
    suspend fun createUser(user: UserAccount) {
        fireStore.collection("users").add(user).await()
    }

    suspend fun createConversation(conversation: Conversation) {
        fireStore.collection("conversations").add(conversation).await()
    }

    fun consultUser(
        callbackNavController: CallbackNavControllerNavigationToChatScreen,
        numberPhone: String, name:String

    ){
        fireStore.collection("users")
            .whereEqualTo("numberPhone", numberPhone.toLong())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        if (document.isEmpty) {
                            println("Document doesn't exist.")
                        } else {
                            println("Document exist.")
                           callbackNavController.invoke()
                        }
                    }
                } else {
                    println("Error ${task.exception}")
                }

                /* for (document in documents){
                     val celPhone = document["numberPhone"].toString()
                     println(celPhone)
                 }*/
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
}
