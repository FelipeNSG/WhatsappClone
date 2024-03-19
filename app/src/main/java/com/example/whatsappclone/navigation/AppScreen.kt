package com.example.whatsappclone.navigation

sealed class AppScreen(val route: String) {
    object LoginScreen : AppScreen("Login")
    object HomeScreen : AppScreen("Home")
    object RegisterScreen: AppScreen("Register")
    object ChatScreen: AppScreen("ChatScreen")
}