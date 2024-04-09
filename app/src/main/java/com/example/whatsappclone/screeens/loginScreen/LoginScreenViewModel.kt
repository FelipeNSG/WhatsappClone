package com.example.whatsappclone.screeens.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.dataStore.DataStoreManager
import kotlinx.coroutines.launch

class LoginScreenViewModel(private val fireStore: FireStoreManager, private val dataStore: DataStoreManager) : ViewModel() {

    fun setPermission(isEnable:Boolean){
        viewModelScope.launch {
            dataStore.setIsEnableToPassToHomeScreen(isEnable)
        }
    }

    fun setUser(userAccount:String){
        viewModelScope.launch {
            dataStore.setUser(userAccount)
        }
    }

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

class MyViewModelFactoryLoginScreen (private val fireStore: FireStoreManager, private val dataStore: DataStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
            LoginScreenViewModel(fireStore, dataStore) as T

        } else throw Exception("Error Factory")
    }
}