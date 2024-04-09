package com.example.whatsappclone.navigation

sealed class AppScreen(val route: String) {
    object SplashScreen :  AppScreen("SplashScreen")
    object LoginScreen : AppScreen("Login")
    object HomeScreen : AppScreen("Home")
    object RegisterScreen: AppScreen("Register")
    object ChatScreen: AppScreen("ChatScreen")
}