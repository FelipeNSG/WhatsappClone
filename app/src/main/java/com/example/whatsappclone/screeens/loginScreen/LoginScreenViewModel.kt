package com.example.whatsappclone.screeens.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.data.FireStoreManager

class LoginScreenViewModel(private val fireStore: FireStoreManager) : ViewModel() {

    fun consulterUser (
        phoneNumberUser:String,
        callBack:(LoginStatedScreen) -> Unit
    ){
        var stated:LoginStatedScreen = LoginStatedScreen.Loading
        callBack(stated)
        fireStore.fetchIfUserExist(phoneNumberUser){
            consulterStated ->
            stated = when(consulterStated){
                FireStoreManager.FireStoreManagerUserConsultState.Error -> {
                    LoginStatedScreen.ErrorConnexion
                }

                FireStoreManager.FireStoreManagerUserConsultState.Loading -> {
                    LoginStatedScreen.Loading
                }

                FireStoreManager.FireStoreManagerUserConsultState.UserNotFound-> {
                    LoginStatedScreen.IncorrectNumber
                }

                FireStoreManager.FireStoreManagerUserConsultState.UserFound -> {
                    LoginStatedScreen.CorrectNumber
                }
            }
            callBack(stated)
        }
    }

    sealed class LoginStatedScreen {
        data object Loading : LoginStatedScreen()
        data object CorrectNumber : LoginStatedScreen()
        data object IncorrectNumber : LoginStatedScreen()
        data object ErrorConnexion : LoginStatedScreen()
    }

}

class MyViewModelFactoryLoginScreen (private val fireStore: FireStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
            LoginScreenViewModel(fireStore) as T

        } else throw Exception("Error Factory")
    }
}