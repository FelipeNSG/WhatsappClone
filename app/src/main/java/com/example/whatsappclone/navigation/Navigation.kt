package com.example.whatsappclone.navigation

import androidx.compose.runtime.Composable

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whatsappclone.screeens.homeScreen.HomeScreen
import com.example.whatsappclone.screeens.homeScreen.HomeViewModel
import com.example.whatsappclone.screeens.homeScreen.MyViewModelFactory
import com.example.whatsappclone.screeens.loginScreen.LoginScreen
import com.example.whatsappclone.screeens.registerScreen.RegisterScreen
import com.example.whatsappclone.utils.FireStoreManager

@Composable
fun AppNavigation(homeViewModel: HomeViewModel) {
    val navController = rememberNavController()
    val fireStore = FireStoreManager()
    NavHost(navController = navController, startDestination = AppScreen.LoginScreen.route) {
        composable(AppScreen.LoginScreen.route) {
            LoginScreen(
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
            RegisterScreen(navController, fireStoreManager = fireStore)
        }

        composable(
            AppScreen.ChatScreen.route
        ) {

        }
    }
}

