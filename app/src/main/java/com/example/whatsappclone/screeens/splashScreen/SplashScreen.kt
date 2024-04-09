package com.example.whatsappclone.screeens.splashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.whatsappclone.R
import com.example.whatsappclone.navigation.AppScreen
import kotlinx.coroutines.delay

typealias CallbackNavControllerNavigationToLoginScreen = () -> Unit
typealias CallbackNavControllerNavigationToHomeScreen = () -> Unit

@Composable
fun Splashscreen(
    store: Boolean,
    callbackNavControllerNavigationToLoginScreen: CallbackNavControllerNavigationToLoginScreen,
    callbackNavControllerNavigationToHomeScreen: CallbackNavControllerNavigationToHomeScreen
) {
    var screen by remember {
        mutableStateOf("")
    }
    screen = if (store) {
        AppScreen.HomeScreen.route
    } else {
        AppScreen.LoginScreen.route
    }

    when (screen == AppScreen.HomeScreen.route) {
        true -> {
            LaunchedEffect(key1 = true) {
                delay(2000)
                callbackNavControllerNavigationToLoginScreen.invoke()
            }
        }

        false -> {
            LaunchedEffect(key1 = true) {
                delay(2000)
                callbackNavControllerNavigationToHomeScreen.invoke()
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.whatsapp_image),
            contentDescription = "ImageSplashScreen"
        )
    }

}