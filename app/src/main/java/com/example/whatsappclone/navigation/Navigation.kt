package com.example.whatsappclone.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.screeens.chatScreen.ChatScreen
import com.example.whatsappclone.screeens.chatScreen.ChatScreenViewModel
import com.example.whatsappclone.screeens.chatScreen.MyViewModelFactoryChatScreen
import com.example.whatsappclone.screeens.homeScreen.HomeScreen
import com.example.whatsappclone.screeens.homeScreen.HomeViewModel
import com.example.whatsappclone.screeens.homeScreen.MyViewModelFactory
import com.example.whatsappclone.screeens.loginScreen.LoginScreen
import com.example.whatsappclone.screeens.loginScreen.LoginScreenViewModel
import com.example.whatsappclone.screeens.loginScreen.MyViewModelFactoryLoginScreen
import com.example.whatsappclone.screeens.registerScreen.MyViewModelFactoryRegisterScreen
import com.example.whatsappclone.screeens.registerScreen.RegisterScreen
import com.example.whatsappclone.screeens.registerScreen.RegisterScreenViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val fireStore = FireStoreManager()
    NavHost(navController = navController, startDestination = AppScreen.LoginScreen.route) {
        composable(AppScreen.LoginScreen.route) {
            val loginScreenViewModel: LoginScreenViewModel =
                viewModel(factory = MyViewModelFactoryLoginScreen(fireStore))
            LoginScreen(
                loginScreenViewModel,
                {
                    navController.navigate(route = AppScreen.HomeScreen.route)
                },
                {
                    navController.navigate(route = AppScreen.RegisterScreen.route)
                }
            )
        }
        composable(
            AppScreen.HomeScreen.route
        ) {
            val homeScreenViewModel: HomeViewModel =
                viewModel(factory = MyViewModelFactory(fireStore))
            HomeScreen(
                homeScreenViewModel
            ){
                navController.navigate(route = AppScreen.ChatScreen.route)
            }
        }

        composable(AppScreen.RegisterScreen.route) {
            val registerScreenViewModel: RegisterScreenViewModel =
                viewModel(factory = MyViewModelFactoryRegisterScreen(fireStore))
            RegisterScreen(
                registerScreenViewModel
            ){
                navController.navigate(route = AppScreen.HomeScreen.route)
            }
        }

        composable(
            AppScreen.ChatScreen.route
        ) {
          val chatScreenViewModel : ChatScreenViewModel =
              viewModel(factory = MyViewModelFactoryChatScreen(fireStore))
            ChatScreen(
                chatScreenViewModel
            )
        }
    }
}

