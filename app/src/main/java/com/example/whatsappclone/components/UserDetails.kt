package com.example.whatsappclone.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsappclone.data.moldel.ChatListDataObject
import com.example.whatsappclone.ui.theme.GreenWhatsapp

@Composable
fun UserDetails(chatData: ChatListDataObject) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        MessageHeader(chatData)
        MessageSubsection(chatData)
    }
}

@Composable
fun MessageHeader(chatData: ChatListDataObject) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextComponent(
            modifier = Modifier.weight(1f),
            value = chatData.userName,
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
        TextComponent(
            value = chatData.message.timeStamp,
            fontSize = 12.sp,
            color = if ((chatData.message.unreadCount ?: 0) > 0) GreenWhatsapp else Color.Gray,
            modifier = null
        )
    }
}

@Composable
fun MessageSubsection(chatData: ChatListDataObject) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (chatData.message.content != null) {
            TextComponent(
                value = chatData.message.content,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold
            )
            chatData.message.unreadCount?.also {
                CircularCount(
                    unreadCount = it.toString(),
                    backgroundColor = GreenWhatsapp,
                    textColor = Color.White
                )
            }
        }
    }
}


















