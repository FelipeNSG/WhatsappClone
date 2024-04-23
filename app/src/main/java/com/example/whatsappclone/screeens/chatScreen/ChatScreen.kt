package com.example.whatsappclone.screeens.chatScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.whatsappclone.components.TopAppBarChatScreen
import com.example.whatsappclone.ui.theme.GreenButtons
import com.example.whatsappclone.ui.theme.colorBlueChat
import com.example.whatsappclone.ui.theme.colorChatGreen
import com.example.whatsappclone.ui.theme.colorGreyChat


@Composable
fun ChatScreen(chatScreenViewModel: ChatScreenViewModel) {
    val profileImage = remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) { profileImage.value = chatScreenViewModel.getImage() }
    Scaffold(
        topBar = { TopAppBarChatScreen(chatScreenViewModel.userAlias, profileImage.value) },
        bottomBar = {
            TextFieldChatAndAdjacentButtons(chatScreenViewModel)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            ChatBox(chatScreenViewModel)
        }
    }
}

@Composable
fun TextFieldChatAndAdjacentButtons(
    chatScreenViewModel: ChatScreenViewModel,
) {
    val showButtonSend = remember {
        mutableStateOf(false)
    }
    val message = rememberSaveable {
        mutableStateOf("")
    }


    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.size(45.dp),
            onClick = {
            }
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "AddButtons",
                modifier = Modifier.size(45.dp),
                tint = GreenButtons
            )
        }
        TextField(
            modifier = Modifier
                .width(240.dp)
                .height(53.dp),
            value = message.value,
            onValueChange = {
                message.value = it
                showButtonSend.value = message.value.isNotEmpty()
            },
            placeholder = {
                Row {
                    Text(
                        text = "Type message...",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                    }) {
                        Icon(
                            imageVector = Icons.Default.SentimentSatisfied,
                            contentDescription = "Icon Face",
                            tint = GreenButtons
                        )
                    }
                }
            },
            shape = CircleShape,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        if (!showButtonSend.value) {
            ShowButtonsCameraAndMicrophone()

        } else {
            ShowSendButton(
                chatScreenViewModel = chatScreenViewModel,
                contentMessage = message.value
            ) {
                message.value = it
            }
        }
    }
}

@Composable
fun ShowButtonsCameraAndMicrophone(

) {
    IconButton(
        modifier = Modifier.size(40.dp),
        onClick = { }
    ) {
        Icon(
            imageVector = Icons.Filled.Mic,
            contentDescription = "Buttons",
            modifier = Modifier.size(36.dp),
            tint = Color.Gray
        )
    }
    IconButton(
        modifier = Modifier.size(40.dp),
        onClick = { }
    ) {
        Icon(
            imageVector = Icons.Filled.PhotoCamera,
            contentDescription = "Buttons",
            modifier = Modifier.size(35.dp),
            tint = Color.Gray
        )
    }
}

@Composable
fun ShowSendButton(
    chatScreenViewModel: ChatScreenViewModel,
    contentMessage: String,
    messageCallBack: (String) -> Unit
) {
    val chatExist = remember {
        mutableStateOf(false)
    }
    println(chatExist.value)
    IconButton(
        modifier = Modifier.size(40.dp),
        onClick = {
            if (!chatExist.value) {
                println(chatScreenViewModel.userLogPhoneAccount)
                println(chatScreenViewModel.numberContact)
                chatScreenViewModel.checkIfAChatAlreadyExists(
                    chatScreenViewModel.userLogPhoneAccount,
                    chatScreenViewModel.numberContact,
                    chatScreenViewModel.userAlias,
                    contentMessage,
                ) { statedFetchChat ->
                    when (statedFetchChat) {
                        ChatScreenViewModel.ChatScreenStated.FoundChat -> {
                            chatScreenViewModel.sendMessage(
                                chatScreenViewModel.userLogPhoneAccount,
                                chatScreenViewModel.numberContact,
                                contentMessage
                            )
                            chatExist.value = true
                        }

                        ChatScreenViewModel.ChatScreenStated.NotFountChat -> {
                            chatExist.value = true
                        }

                        else -> {
                            Unit
                        }
                    }
                }

            } else {
                chatScreenViewModel.sendMessage(
                    chatScreenViewModel.userLogPhoneAccount,
                    chatScreenViewModel.numberContact,
                    contentMessage
                )
            }
            messageCallBack("")
        }
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Button Send",
            modifier = Modifier.size(35.dp),
            tint = GreenButtons
        )
    }
}

@Composable
fun ChatBox(
    chatScreenViewModel: ChatScreenViewModel,
) {
    val chatList by chatScreenViewModel.getChat().collectAsState(emptyList())
    if (chatList.isNotEmpty()) {
        LazyColumn(
            reverseLayout = true
        ) {
            items(chatList.first().messages.size) {
                if (chatScreenViewModel.userLogPhoneAccount == chatList.first().messages[(chatList.first().messages.size) - (it + 1)].user) {
                    ChatMessageTest1(message = chatList.first().messages[(chatList.first().messages.size) - (it + 1)].content)
                } else {
                    if (chatScreenViewModel.userLogPhoneAccount == chatList.first().dataUser1.numberPhone.toString()) {
                        ChatTesting2(
                            message = chatList.first().messages[(chatList.first().messages.size) - (it + 1)].content,
                            chatList.first().dataUser2.userImage
                        )
                    } else {
                        ChatTesting2(
                            message = chatList.first().messages[(chatList.first().messages.size) - (it + 1)].content,
                            chatList.first().dataUser1.userImage
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageTest1(message: String) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        Text(
            text = "9:10",
            color = Color.Gray,
            fontSize = 10.sp,
            modifier = Modifier.padding(end = 10.dp)
        )
        Icon(
            imageVector = Icons.Filled.DoneAll,
            contentDescription = "check",
            modifier = Modifier.size(15.dp),
            tint = colorBlueChat
        )
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = colorChatGreen
            ),
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}


@Composable
fun ChatTesting2(message: String, imageUrl: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = 0.dp,
                bottomEnd = 12.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = colorGreyChat
            ),
        ) {
            Text(
                message,
                modifier = Modifier.padding(10.dp)
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .size(20.dp),
            shape = CircleShape,
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "User Photo",
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
        Text(
            text = "12:10",
            color = Color.Gray,
            fontSize = 10.sp,
            modifier = Modifier.padding(start = 10.dp)
        )
    }

}

