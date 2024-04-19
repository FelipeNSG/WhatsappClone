package com.example.whatsappclone.screeens.registerScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsappclone.ui.theme.GreenWhatsapp

typealias CallbackNavControllerToHomeScreen = (String) -> Unit
typealias CallbackNavControllerToRegisterScreen = () -> Unit

@Composable
fun RegisterScreen(
    viewModel: RegisterScreenViewModel,
    callbackNavControllerToHomeScreen: CallbackNavControllerToHomeScreen
) {

    val phoneNumber = remember {
        mutableStateOf("")
    }
    val phoneNumberConfirmation = remember {
        mutableStateOf("")
    }
    val numberMatch = remember {
        mutableStateOf(true)
    }
    val numberPhoneAlreadyExist = remember {
        mutableStateOf(false)
    }

    val errorConnexion = remember {
        mutableStateOf(false)
    }
    val numberRegisterSuccess = remember {
        mutableStateOf(false)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create an account",
            fontSize = 30.sp,
        )
        Spacer(modifier = Modifier.padding(bottom = 40.dp))
        TextField(
            value = phoneNumber.value,
            onValueChange = {
                phoneNumber.value = it
            },
            placeholder = {
                Text(text = "Enter your number phone")
            }
        )
        Spacer(modifier = Modifier.padding(bottom = 10.dp))

        TextField(
            value = phoneNumberConfirmation.value,
            onValueChange = { phoneNumberConfirmation.value = it },
            placeholder = {
                Text(text = "Confirm your number phone")
            }
        )
        Spacer(modifier = Modifier.padding(bottom = 5.dp))

        if (!numberMatch.value) {
            Text(
                text = "The phone number does not match",
                fontSize = 15.sp,
                color = Color.Red
            )

        }
        if (numberPhoneAlreadyExist.value) {
            Text(
                text = "The phone number already exist",
                fontSize = 15.sp,
                color = Color.Red
            )

        }
        if (errorConnexion.value) {
            Text(
                text = "Error of connexion",
                fontSize = 15.sp,
                color = Color.Red
            )

        }
        if (numberRegisterSuccess.value) {
            Text(
                text = "number registered correctly",
                fontSize = 18.sp,
                color = GreenWhatsapp
            )
        }

        Button(
            onClick = {
                numberPhoneAlreadyExist.value = false
                errorConnexion.value = false
                numberRegisterSuccess.value = false
                numberMatch.value = true

                if (phoneNumber.value != phoneNumberConfirmation.value) {
                    numberMatch.value = false
                }

                viewModel.checkNumbersAndRegister(
                    phoneNumber.value.toLong(),
                    phoneNumberConfirmation.value.toLong(),
                ) { registerStatedScreen ->
                    when (registerStatedScreen) {
                        RegisterScreenViewModel.RegisterStatedScreen.ErrorConnexion -> {
                            errorConnexion.value = true
                        }
                        RegisterScreenViewModel.RegisterStatedScreen.Loading -> {
                            Unit
                        }

                        RegisterScreenViewModel.RegisterStatedScreen.NumberExist -> {
                            numberPhoneAlreadyExist.value = true
                        }

                        RegisterScreenViewModel.RegisterStatedScreen.NumberRegister -> {
                            numberRegisterSuccess.value = true
                            callbackNavControllerToHomeScreen.invoke("/${phoneNumber.value}")
                        }

                        else -> {Unit}
                    }
                }
            }
        ) {
            Text(text = "Enter")
        }

    }
}



