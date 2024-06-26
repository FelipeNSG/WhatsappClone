package com.example.whatsappclone.screeens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.navigation.AppNavigation

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        super.onCreate(savedInstanceState)
        val sp = installSplashScreen()

        sp.setKeepOnScreenCondition {
            when (viewModel.getAllow()) {
                true -> {
                    println(viewModel.getPermissionToPass())
                    println("este es el user ${viewModel.getUserId()}")
                    setContent {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            AppNavigation(
                                viewModel.getUserId(),
                            )
                        }
                    }
                    return@setKeepOnScreenCondition false
                }

                false -> {
                    println(viewModel.getPermissionToPass())
                    println("este es el user ${viewModel.getUserId()}")
                    return@setKeepOnScreenCondition true
                }
            }
        }
    }
}


