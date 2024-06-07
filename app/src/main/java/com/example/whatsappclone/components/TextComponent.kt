package com.example.whatsappclone.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Composable
fun TextComponent(
    value: String,
    fontSize: TextUnit,
    color: Color,
    modifier: Modifier?,
    fontWeight: FontWeight? = FontWeight.Normal
) {
    var text = value
    text = if(value.length >28){

        value.take(28).plus("...")
    }
    else {
        value
    }

    if (modifier != null) {
        Text(
            modifier = modifier,
            text =text,
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = color
            )
        )
    } else {
        Text(
            text = value,
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = color
            )
        )
    }
}





























