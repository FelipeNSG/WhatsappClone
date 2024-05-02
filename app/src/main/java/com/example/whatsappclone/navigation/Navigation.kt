package com.example.whatsappclone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.whatsappclone.data.AuthenticationFirebaseManager
import com.example.whatsappclone.data.FireStoreManager
import com.example.whatsappclone.data.FirebaseStorageManager
import com.example.whatsappclone.dataStore.DataStoreSingleton
import com.example.whatsappclone.screeens.chatScreen.ChatScreen
import com.example.whatsappclone.screeens.chatScreen.ChatScreenViewModel
import com.example.whatsappclone.screeens.chatScreen.MyViewModelFactoryChatScreen
import com.example.whatsappclone.screeens.homeScreen.HomeScreen
import com.example.whatsappclone.screeens.homeScreen.HomeViewModel
import com.example.whatsappclone.screeens.homeScreen.MyViewModelFactory
import com.example.whatsappclone.screeens.loginScreen.LogInScreen
import com.example.whatsappclone.screeens.loginScreen.LoginScreenViewModel
import com.example.whatsappclone.screeens.loginScreen.MyViewModelFactoryLoginScreen
import com.example.whatsappclone.screeens.registerUserNameScreen.MyViewModelFactoryRegisterUserNameScreenViewModel
import com.example.whatsappclone.screeens.registerUserNameScreen.RegisterUserNameScreen
import com.example.whatsappclone.screeens.registerUserNameScreen.RegisterUserNameScreenViewModel
import com.example.whatsappclone.screeens.verifyScreen.MyViewModelFactoryVerifyScreen
import com.example.whatsappclone.screeens.verifyScreen.OTP_VerifyScreen
import com.example.whatsappclone.screeens.verifyScreen.VerifyScreenViewModel

@Composable
fun AppNavigation(
    loginUser: String,
) {

    val navController = rememberNavController()
    val storage = FirebaseStorageManager()
    val fireStore = FireStoreManager()
    val firebaseAuth = AuthenticationFirebaseManager()
    val dataStore = DataStoreSingleton.getInstance(LocalContext.current)
    NavHost(
        navController = navController,
        startDestination = if (loginUser.isEmpty()) AppScreen.LoginScreen.route else {
            AppScreen.HomeScreen.route + "/$loginUser"
        }
    ) {
        composable(
            AppScreen.LoginScreen.route
        ) {
            val loginScreenViewModel: LoginScreenViewModel =
                viewModel(factory = MyViewModelFactoryLoginScreen(fireStore, firebaseAuth))
            LogInScreen(
                loginScreenViewModel,
            ) {
                navController.navigate(route = AppScreen.VerifyScreen.route + it)
            }
        }

        composable(
            AppScreen.VerifyScreen.route + "/{storedVerificationId}/{numberPhone}",
            arguments = listOf(
                navArgument(name = "storedVerificationId") {
                    type = NavType.StringType
                },
                navArgument(name = "numberPhone") {
                    type = NavType.StringType
                }
            )
        ) {
            val storedVerificationId =
                it.arguments?.getString("storedVerificationId") ?: "no_IdFound"
            val numberPhone = it.arguments?.getString("numberPhone") ?: "no_numberPhone"
            val verifyScreenViewModel: VerifyScreenViewModel =
                viewModel(
                    factory = MyViewModelFactoryVerifyScreen(
                        numberPhone,
                        fireStore,
                        firebaseAuth,
                        storedVerificationId
                    )
                )
            OTP_VerifyScreen(
                verifyScreenViewModel,
                { sendVariables ->
                    navController.navigate(route = AppScreen.RegisterUserNameScreen.route + sendVariables)
                }
            ) { sendVariables ->
                navController.navigate(route = AppScreen.HomeScreen.route + sendVariables)
            }
        }

        composable(
            AppScreen.RegisterUserNameScreen.route + "/{numberPhone}",
            arguments = listOf(
                navArgument(name = "numberPhone") {
                    type = NavType.StringType
                }
            )
        ) {
            val userNumberPhone = it.arguments?.getString("numberPhone") ?: "no_numberPhone"
            val registerUserNameScreenViewModel: RegisterUserNameScreenViewModel = viewModel(
                factory = MyViewModelFactoryRegisterUserNameScreenViewModel(
                    userNumberPhone,
                    fireStore,
                    firebaseAuth
                )
            )
            RegisterUserNameScreen(registerUserNameScreenViewModel) { dataPath ->
                navController.navigate(route = AppScreen.HomeScreen.route + dataPath)
            }
        }

        composable(
            AppScreen.HomeScreen.route + "/{userLog}",
            arguments = listOf(
                navArgument(name = "userLog") {
                    type = NavType.StringType
                }
            )
        ) {
            val userLog: String = it.arguments?.getString("userLog") ?: loginUser
            val homeScreenViewModel: HomeViewModel =
                viewModel(factory = MyViewModelFactory(userLog, fireStore, firebaseAuth, dataStore))
            HomeScreen(
                homeScreenViewModel,
                { variables ->
                    navController.navigate(route = AppScreen.ChatScreen.route + variables)
                }
            ) { navController.navigate(route = AppScreen.LoginScreen.route) }
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
                        storage,
                        contactToAdd,
                        userNameContactToAdd,
                        userPhoneAccount,
                        idDocument
                    )
                )
            ChatScreen(
                chatScreenViewModel,
            ){navController.popBackStack()}
        }
    }
}

