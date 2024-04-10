package com.example.whatsappclone.screeens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.data.moldel.ChatBoxObject
import com.example.whatsappclone.dataStore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(
    val logUser: String,
    private val fireStore: FireStoreManager,
    private val dataStore: DataStoreManager
) : ViewModel() {

    fun sendDataToDataStore(){
        viewModelScope.launch {
            dataStore.setUser(logUser)
            dataStore.setIsEnableToPassToHomeScreen(true)
        }
    }

    fun removeSession(){
        viewModelScope.launch {
            dataStore.setUser("")
        }
    }
    fun getChatList(): Flow<List<ChatBoxObject>> {
        return fireStore.getListChatBox(logUser)
    }

    fun userConsulting(
        numberPhoneContact: String,
        callBack: (HomeScreenStated) -> Unit
    ) {
        var sated: HomeScreenStated = HomeScreenStated.Loading
        if (logUser != numberPhoneContact) {
            callBack(sated)
            fireStore.fetchIfUserExist(
                numberPhoneContact,
            ) { statedFireStore ->
                sated = when (statedFireStore) {
                    FireStoreManager.FireStoreManagerUserConsultState.Error -> {
                        HomeScreenStated.ErrorConnexion
                    }

                    FireStoreManager.FireStoreManagerUserConsultState.Loading -> {
                        HomeScreenStated.Loading
                    }

                    FireStoreManager.FireStoreManagerUserConsultState.UserNotFound -> {
                        HomeScreenStated.IncorrectNumber
                    }

                    FireStoreManager.FireStoreManagerUserConsultState.UserFound -> {
                        HomeScreenStated.CorrectNumber

                    }
                }
                callBack(sated)
            }
        } else {
            callBack(HomeScreenStated.IncorrectNumber)
        }
    }

    sealed class HomeScreenStated {
        data object Loading : HomeScreenStated()
        data object CorrectNumber : HomeScreenStated()
        data object IncorrectNumber : HomeScreenStated()
        data object ErrorConnexion : HomeScreenStated()
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