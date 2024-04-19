package com.example.whatsappclone.screeens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.data.moldel.ChatBoxObject
import com.example.whatsappclone.dataStore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class HomeViewModel(
    val logUser: String,
    private val fireStore: FireStoreManager,
    private val dataStore: DataStoreManager
) : ViewModel() {

    /*fun sendDataToDataStore() {
        viewModelScope.launch {
            dataStore.setUser(logUser)
            dataStore.setIsEnableToPassToHomeScreen(true)
        }
    }

    fun removeSession() {
        viewModelScope.launch {
            dataStore.setUser("")
        }
    }*/

    suspend fun consulterChat(
        logUser: String,
        numberPhoneContact: String,
    ): HomeScreenStated = suspendCancellableCoroutine { continuation ->
        fireStore.consulterChat(
            logUser,
            numberPhoneContact
        ) { fireStoreManagerState ->
            when (fireStoreManagerState) {
                FireStoreManager.FireStoreManagerState.ChatFound -> {
                    continuation.resume(HomeScreenStated.TheChatAlreadyExist)
                }

                FireStoreManager.FireStoreManagerState.ChatNotFound -> {
                    continuation.resume(HomeScreenStated.CorrectNumber)
                }

                FireStoreManager.FireStoreManagerState.Error -> {
                    continuation.resume(HomeScreenStated.ErrorConnexion)
                }

                FireStoreManager.FireStoreManagerState.Loading -> {
                    continuation.resume(HomeScreenStated.Loading)

                }
            }
        }
    }

    fun verify(
        logUser: String,
        numberPhoneContact: String,
        callbackSated: (HomeScreenStated) -> Unit
    ) {
        var stated: HomeScreenStated = HomeScreenStated.Loading
        viewModelScope.launch {
            val userConsultResult = userConsulting(numberPhoneContact)
            if (userConsultResult == HomeScreenStated.CorrectNumber) {
                val consulterChatResult = consulterChat(logUser, numberPhoneContact)
                stated = if (consulterChatResult == HomeScreenStated.TheChatAlreadyExist) {
                    HomeScreenStated.TheChatAlreadyExist
                } else {
                    HomeScreenStated.CorrectNumber
                }
            } else {
                stated = HomeScreenStated.ErrorConnexion
                callbackSated(stated)
            }
            callbackSated(stated)
        }
    }

    fun getChatList(): Flow<List<ChatBoxObject>> {
        return fireStore.getListChatBox(logUser)
    }

    private suspend fun userConsulting(
        numberPhoneContact: String,
    ): HomeScreenStated = suspendCancellableCoroutine { continuation ->
        if (logUser != numberPhoneContact) {
            println("hellegado aqui")
            fireStore.fetchIfUserExist(
                numberPhoneContact,
            ) { statedFireStore ->
                when (statedFireStore) {

                    FireStoreManager.FireStoreManagerUserConsultState.Error -> {
                        continuation.resume(HomeScreenStated.ErrorConnexion)
                    }

                    FireStoreManager.FireStoreManagerUserConsultState.Loading -> {
                        continuation.resume(HomeScreenStated.Loading)
                    }

                    FireStoreManager.FireStoreManagerUserConsultState.UserNotFound -> {
                        continuation.resume(HomeScreenStated.IncorrectNumber)
                    }

                    FireStoreManager.FireStoreManagerUserConsultState.UserFound -> {
                        continuation.resume(HomeScreenStated.CorrectNumber)
                    }
                }
            }
        } else {
            HomeScreenStated.IncorrectNumber
        }
    }

    sealed class HomeScreenStated {
        data object Loading : HomeScreenStated()
        data object CorrectNumber : HomeScreenStated()
        data object IncorrectNumber : HomeScreenStated()
        data object ErrorConnexion : HomeScreenStated()
        data object TheChatAlreadyExist : HomeScreenStated()
    }
}

class MyViewModelFactory(
    private val numberPhone: String,
    private val fireStore: FireStoreManager,
    private val dataStore: DataStoreManager
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(numberPhone, fireStore, dataStore) as T

        } else throw Exception("Error Factory")
    }
}