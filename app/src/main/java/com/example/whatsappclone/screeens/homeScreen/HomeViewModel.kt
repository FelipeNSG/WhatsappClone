package com.example.whatsappclone.screeens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.data.moldel.ChatBoxObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    val logUser: String,
    private val fireStore: FireStoreManager
) : ViewModel() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var _userList = MutableSharedFlow<List<ChatBoxObject>>()
    val userList: Flow<List<ChatBoxObject>> = _userList

    fun userConsulting(
        numberPhoneContact: String,
        callBack: (HomeScreenStated) -> Unit
    ) {
        var sated: HomeScreenStated = HomeScreenStated.Loading
        if (logUser != numberPhoneContact) {
            println(logUser)
            println(numberPhoneContact)
            callBack(sated)
            fireStore.fetchUser(
                numberPhoneContact,
            ) { statedFireStore ->
                sated = when (statedFireStore) {
                    FireStoreManager.FireStoreManagerState.Error -> {
                        HomeScreenStated.ErrorConnexion
                    }

                    FireStoreManager.FireStoreManagerState.Loading -> {
                        HomeScreenStated.Loading
                    }

                    FireStoreManager.FireStoreManagerState.NoSuccess -> {
                        HomeScreenStated.IncorrectNumber
                    }

                    FireStoreManager.FireStoreManagerState.Success -> {
                        HomeScreenStated.CorrectNumber
                    }
                }
                callBack(sated)
            }
        }else {
            callBack(HomeScreenStated.IncorrectNumber)
        }
    }

    fun createChatMessages(

    ) {

    }

    fun updateFlow() {
        scope.launch {

        }
    }


    fun createChatScreen() {

    }

    sealed class HomeScreenStated {
        data object Loading : HomeScreenStated()
        data object CorrectNumber : HomeScreenStated()
        data object IncorrectNumber : HomeScreenStated()
        data object ErrorConnexion : HomeScreenStated()
    }

}

class MyViewModelFactory(val numberPhone: String, private val fireStore: FireStoreManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(numberPhone, fireStore) as T

        } else throw Exception("Error Factory")
    }
}