package com.example.whatsappclone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.dataStore.DataStoreSingleton
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
fun AppNavigation(
  loginUser:String?,

) {
    val navController = rememberNavController()
    val fireStore = FireStoreManager()
    val dataStore = DataStoreSingleton.getInstance(LocalContext.current)

    NavHost(
        navController = navController,

        startDestination = if (loginUser == null) {
            AppScreen.LoginScreen.route
        } else AppScreen.HomeScreen.route + "/${loginUser}"
    ) {


        composable(
            AppScreen.LoginScreen.route
        ) {
            val loginScreenViewModel: LoginScreenViewModel =
                viewModel(factory = MyViewModelFactoryLoginScreen(fireStore, dataStore))
            LoginScreen(
                loginScreenViewModel,
                {
                    navController.navigate(route = AppScreen.HomeScreen.route + it)
                },
                {
                    navController.navigate(route = AppScreen.RegisterScreen.route)
                }
            )
        }

        composable(
            AppScreen.HomeScreen.route + "/{userLog}",
            arguments = listOf(
                navArgument(name = "userLog") {
                    type = NavType.StringType
                }
            )

        ) {
            val userLog: String = it.arguments?.getString("userLog") ?: loginUser ?: "Unknown"
            val homeScreenViewModel: HomeViewModel =
                viewModel(factory = MyViewModelFactory(userLog, fireStore))
            HomeScreen(
                homeScreenViewModel,
                { navController.navigate(route = AppScreen.LoginScreen.route) }
            )
            { sendVariables -> navController.navigate(route = AppScreen.ChatScreen.route + sendVariables) }
        }

        composable(AppScreen.RegisterScreen.route) {
            val registerScreenViewModel: RegisterScreenViewModel =
                viewModel(factory = MyViewModelFactoryRegisterScreen(fireStore))
            RegisterScreen(
                registerScreenViewModel
            ) {
                navController.navigate(route = AppScreen.HomeScreen.route + it)
            }
        }

        composable(
            AppScreen.ChatScreen.route + "/{contactToAdd}/{userNameContactToAdd}/{userPhoneAccount}/{idDocument}",
            arguments = listOf(
                navArgument(name = "contactToAdd") {
                    type = NavType.StringType
                },
                navArgument(name = "userNameContactToAdd") {
                    type = NavType.StringType
                },
                navArgument(name = "userPhoneAccount") {
                    type = NavType.StringType
                },
                navArgument(name = "idDocument") {
                    type = NavType.StringType
                }
            )
        ) {
            val contactToAdd: String = it.arguments?.getString("contactToAdd") ?: "unknown"
            val userNameContactToAdd: String =
                it.arguments?.getString("userNameContactToAdd") ?: "unknown"
            val userPhoneAccount: String = it.arguments?.getString("userPhoneAccount") ?: "unknown"
            val idDocument: String = it.arguments?.getString("idDocument") ?: "unknown"
            val chatScreenViewModel: ChatScreenViewModel =
                viewModel(
                    factory = MyViewModelFactoryChatScreen(
                        fireStore,
                        contactToAdd,
                        userNameContactToAdd,
                        userPhoneAccount,
                        idDocument
                    )
                )
            ChatScreen(
                chatScreenViewModel,
            )
        }
    }
}

