package com.example.whatsappclone.screeens.loginScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp


typealias CallbackNavControllerToHomeScreen = (String) -> Unit
typealias CallbackNavControllerToRegisterScreen = () -> Unit

@Composable
fun PhoneNumberAuthorizationScreen(
    loginScreenViewModel: LoginScreenViewModel,
    callbackNavControllerNavigationToHomeScreen: CallbackNavControllerToHomeScreen,
    callbackNavControllerNavigationToRegisterScreen: CallbackNavControllerToRegisterScreen
) {
    val userCorrect = remember {
        mutableStateOf(true)
    }
    val userLog = rememberSaveable {
        mutableStateOf("")
    }

    val errorConnection = remember {
        mutableStateOf(false)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "logIn",
            fontSize = 40.sp
        )

        TextField(
            value = userLog.value,
            onValueChange = { userLog.value = it },
            placeholder = { Text(text = "Enter your phone number") }
        )

        if (!userCorrect.value) {
            Text(
                text = "the number is incorrect",
                color = Color.Red
            )
        }
        if (errorConnection.value) {
            Text(
                text = "connection error",
                color = Color.Red
            )
        }

        Button(
            onClick = {
                userCorrect.value = true
                errorConnection.value = false
                if (userLog.value.length > 1) {
                    loginScreenViewModel.consulterUser(
                        userLog.value,
                    ) { requestStated ->
                        when (requestStated) {
                            LoginScreenViewModel.LoginStatedScreen.ErrorConnexion -> {
                                errorConnection.value = true
                                println("error")
                            }

                            LoginScreenViewModel.LoginStatedScreen.Loading -> {
                                println("loading")
                            }

                            LoginScreenViewModel.LoginStatedScreen.IncorrectNumber-> {
                                userCorrect.value = false
                                println("incorrect number")
                            }

                            LoginScreenViewModel.LoginStatedScreen.CorrectNumber-> {
                                callbackNavControllerNavigationToHomeScreen.invoke("/${userLog.value}")
                                println("correct")
                            }

                        }
                    }
                }
            }
        ) {
            Text(text = "Enter")
        }

        Text(
            text = "Create Account",
            color = Color.Gray,
            fontSize = 20.sp,
            modifier = Modifier.clickable {
                callbackNavControllerNavigationToRegisterScreen.invoke()
            }
        )
    }
}
