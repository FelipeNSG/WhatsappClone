package com.example.whatsappclone.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsappclone.screeens.homeScreen.CallbackNavControllerNavigationToLoginScreen
import com.example.whatsappclone.screeens.homeScreen.CallbackRemoveSession
import com.example.whatsappclone.ui.theme.fontFamilyMonserrat


@Composable
fun AppBarHomeScreen(
    userLog:String,
    callbackNavController: CallbackNavControllerNavigationToLoginScreen,
    removeSession: CallbackRemoveSession,

) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "WhatsApp",
            fontFamily = fontFamilyMonserrat(500),
            fontSize = 15.sp,
            color = Color.Black,
        )
      /*  Text(
            text = "Welcome: $userLog",
            fontFamily = fontFamilyMonserrat(500),
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.padding(10.dp)
        )*/
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    callbackNavController.invoke()
                    removeSession.invoke()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Icon Settings",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

}
