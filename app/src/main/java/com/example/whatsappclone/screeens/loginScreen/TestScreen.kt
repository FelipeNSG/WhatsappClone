package com.example.whatsappclone.screeens.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


@Composable
@Preview
fun PhoneNumberAuthorizationScreenPreview() {
    val maxNumbers = 10
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    val phoneNumber = remember {
        mutableStateOf("")
    }

    if (phoneNumber.value.length == maxNumbers+1){
        println(CountryCodePicker.getPhoneNumber())
    }
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
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Image(
            modifier = Modifier.size(175.dp),
            painter = painterResource(id = R.drawable.mobile_phone),
            contentDescription = "Image"
        )
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Enter mobile number",
                fontSize = 25.sp
            )
        }
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
        Button(
            modifier = Modifier.width(100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenWhatsapp),
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = "Send",
                fontSize = 20.sp
            )
        }
    }
}