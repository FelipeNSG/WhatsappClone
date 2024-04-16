package com.example.whatsappclone.screeens.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsappclone.R
import com.example.whatsappclone.ui.theme.GreenButtons
import com.example.whatsappclone.ui.theme.GreenWhatsapp

@Composable
@Preview
fun RegisterUserNameScreen() {
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    var text by remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    val maxText = remember {
        mutableIntStateOf(15)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 50.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    focusManager.clearFocus()
                }
            ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .width(120.dp)
                .height(140.dp),
            painter = painterResource(id = R.drawable.image_user),
            contentDescription = "Image_password",
        )
        Text(
            text = "Enter you user name",
            fontSize = 20.sp
        )
        OutlinedTextField(
            value = text,
            onValueChange = { if (it.text.length <= maxText.intValue) text = it },
            placeholder = { Text(text = "Username") },
            shape = MaterialTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Gray,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = GreenButtons, // Este es el margen que rodea el textfield
            ),
            maxLines = 1
        )
        Button(
            modifier = Modifier.width(270.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenWhatsapp),
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = "Register",
                fontSize = 17.sp
            )
        }
    }
}