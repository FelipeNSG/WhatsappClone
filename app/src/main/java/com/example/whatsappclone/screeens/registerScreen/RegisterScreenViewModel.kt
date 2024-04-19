package com.example.whatsappclone.screeens.registerScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.data.moldel.UserAccount
import kotlinx.coroutines.launch

class RegisterScreenViewModel(private val fireStore: FireStoreManager) : ViewModel() {

    private suspend fun userRegister(accountNumber: Long) {
        val newUser = UserAccount(accountNumber.toString())
        fireStore.createUser(newUser){}
    }

    fun checkNumbersAndRegister(
        numberPhone: Long,
        numberPhoneAgain: Long,
        callBack: (RegisterStatedScreen) -> Unit
    ) {
        var stated: RegisterStatedScreen = RegisterStatedScreen.Loading
        callBack(stated)
        if (numberPhone == numberPhoneAgain) {
            fireStore.fetchIfUserExist(numberPhone.toString()) { fireStoreStated ->
                when (fireStoreStated) {
                    FireStoreManager.FireStoreManagerUserConsultState.Error -> {
                        //Error de red
                        stated = RegisterStatedScreen.ErrorConnexion
                    }

                    FireStoreManager.FireStoreManagerUserConsultState.Loading -> {
                        stated = RegisterStatedScreen.Loading
                    }

                    FireStoreManager.FireStoreManagerUserConsultState.UserNotFound-> {
                        //Is NumberRegister because the user was not found and can register
                        stated = RegisterStatedScreen.NumberRegister
                        viewModelScope.launch {
                            userRegister(numberPhone)
                        }
                    }

                    FireStoreManager.FireStoreManagerUserConsultState.UserFound-> {
                        //Is NoSuccess because the user was  found and can't register
                        stated = RegisterStatedScreen.NumberExist
                    }
                }
                callBack(stated)
            }

        }
    }

    sealed class RegisterStatedScreen {
        data object Loading : RegisterStatedScreen()
        data object NumberExist : RegisterStatedScreen()
        data object NumberRegister : RegisterStatedScreen()
        data object ErrorConnexion : RegisterStatedScreen()
    }

}


class MyViewModelFactoryRegisterScreen(private val fireStore: FireStoreManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RegisterScreenViewModel::class.java)) {
            RegisterScreenViewModel(fireStore) as T

        } else throw Exception("Error Factory")
    }

}