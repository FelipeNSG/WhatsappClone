package com.example.whatsappclone.components

/*@Composable
fun UserDetails(chatData: ChatBoxObject) {
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
fun MessageHeader(chatData: ChatBoxObject) {
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
            value = chatData.timeStamp,
            fontSize = 12.sp,
            color = if ((chatData.message.unreadCount ?: 0) > 0) GreenWhatsapp else Color.Gray,
            modifier = null
        )
    }
}

@Composable
fun MessageSubsection(chatData: ChatBoxObject) {
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
}*/


















