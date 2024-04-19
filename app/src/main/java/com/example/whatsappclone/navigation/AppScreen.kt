package com.example.whatsappclone.navigation

sealed class AppScreen(val route: String) {
    object LoginScreen : AppScreen("Login")
    object VerifyScreen: AppScreen("Verify")
    object RegisterUserNameScreen: AppScreen("RegisterUserName")
    object HomeScreen : AppScreen("Home")
    object ChatScreen: AppScreen("ChatScreen")
}