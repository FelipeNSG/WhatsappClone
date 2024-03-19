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
import androidx.navigation.NavController
import com.example.whatsappclone.data.actualUser
import com.example.whatsappclone.navigation.AppScreen

//TODO(REMOVE navController)

@Composable
fun LoginScreen(navController: NavController, ) {
    val userCorrect = remember {
        mutableStateOf(true)
    }
    val userLog = rememberSaveable {
        mutableStateOf("")
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

        Button(
            onClick = {
              /*  if (userLog.value.length > 1 && userLog.value.toLong() in listUsers.map { it.numberPhone } ) {
                    userCorrect.value = true*/
                navController.navigate(route = AppScreen.HomeScreen.route)
                actualUser(userLog.value.toLong())
            }
        ) {
            Text(text = "Enter")
        }

        Text(
            text = "Create Account",
            color = Color.Gray,
            fontSize = 20.sp,
            modifier = Modifier.clickable {
                navController.navigate(route = AppScreen.RegisterScreen.route)
            }
        )
    }
}

