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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsappclone.data.moldel.UserAccount
import com.example.whatsappclone.screeens.loginScreen.CallbackNavControllerToHomeScreen
import kotlinx.coroutines.launch


@Composable
fun RegisterScreen(
    viewModel: RegisterScreenViewModel,
    callbackNavControllerToHomeScreen: CallbackNavControllerToHomeScreen
) {
    val scope = rememberCoroutineScope()
    val text = remember {
        mutableStateOf("")
    }
    val text2 = remember {
        mutableStateOf("")
    }
    val numberMatch = remember {
        mutableStateOf(true)
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
            value = text.value,
            onValueChange = {
                     text.value   = it
            },
            placeholder = {
                Text(text = "Enter your number phone")
            }
        )
        Spacer(modifier = Modifier.padding(bottom = 10.dp))

        TextField(
            value = text2.value,
            onValueChange = {text2.value = it},
            placeholder = {
                Text(text = "Confirm your number phone")
            }
        )
        Spacer(modifier = Modifier.padding(bottom = 5.dp))

        if (
            !numberMatch.value) {
            Text(
                text = "The phone number does not match",
                fontSize = 15.sp,
                color = Color.Red
            )
        }
        Button(
            onClick = {
                scope.launch {
                    viewModel.fireStore.createUser(
                        user = UserAccount(text.value.toLong())
                        )
                   callbackNavControllerToHomeScreen.invoke()
                }
            }
        ) {
            Text(text = "Enter")
        }

    }
}