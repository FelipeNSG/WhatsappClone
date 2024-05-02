package com.example.whatsappclone.screeens.loginScreen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsappclone.R
import com.example.whatsappclone.ui.theme.GreenButtons
import com.example.whatsappclone.ui.theme.GreenWhatsapp
import com.joelkanyi.jcomposecountrycodepicker.component.CountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker

typealias CallbackNavControllerToVerifyScreen = (String) -> Unit

@Composable
fun LogInScreen(
    loginScreenViewModel: LoginScreenViewModel,
    callbackNavControllerNavigationToVerifyScreen: CallbackNavControllerToVerifyScreen,
) {
    val maxNumbers = 10
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    val context: Context = LocalContext.current
    val phoneNumber = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    focusManager.clearFocus()
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(175.dp),
            painter = painterResource(id = R.drawable.mobile_phone),
            contentDescription = "Image"
        )
        Row(
            modifier = Modifier.padding(top = 30.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Enter mobile number",
                fontSize = 25.sp
            )
        }

        Column(
            modifier = Modifier
                .height(110.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KomposeCountryCodePicker(
                modifier = Modifier
                    .width(300.dp),
                text = phoneNumber.value.take(maxNumbers),
                onValueChange = { phoneNumber.value = it },
                placeholder = { Text(text = "Phone Number") },
                shape = MaterialTheme.shapes.extraLarge,
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Gray,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = GreenButtons, // Este es el margen que rodea el textfield
                ),
            )
            if (errorMessage.value) {
                Text(
                    text = "Invalid number",
                    fontSize = 15.sp,
                    color = Color.Red
                )
            }
        }

        Button(
            modifier = Modifier.width(100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenWhatsapp),
            onClick = {
                val result = loginScreenViewModel.checkNumber(
                    CountryCodePicker.getPhoneNumberWithoutPrefix(),
                    CountryCodePicker.getFullPhoneNumber(),
                    context,
                    {
                        callbackNavControllerNavigationToVerifyScreen.invoke("/$it/${CountryCodePicker.getPhoneNumberWithoutPrefix()}")
                    }
                ) {}
                when (result) {
                    true -> errorMessage.value = false

                    false -> {
                        errorMessage.value = true
                    }
                }
            }
        ) {
            Text(
                text = "Send",
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun LogInScreenPreview(

) {
    val maxNumbers = 10
    val interactionSource = remember { MutableInteractionSource() }
    val phoneNumber = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(top = 50.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {

                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(175.dp),
            painter = painterResource(id = R.drawable.mobile_phone),
            contentDescription = "Image"
        )
        Row(
            modifier = Modifier.padding(top = 30.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Enter mobile number",
                fontSize = 25.sp
            )
        }
        Column(
            modifier = Modifier
                .height(110.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .width(300.dp),
                value = "",
                onValueChange = {}
            )

            Text(
                text = "Invalid number",
                fontSize = 15.sp,
                color = Color.Red
            )
        }

        Button(
            modifier = Modifier.width(100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenWhatsapp),
            onClick = {

            }
        ) {
            Text(
                text = "Send",
                fontSize = 20.sp
            )
        }

    }
}