package com.example.whatsappclone.screeens.chatScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType
import androidx.compose.foundation.content.receiveContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.rememberTextFieldState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.whatsappclone.components.TopAppBarChatScreen
import com.example.whatsappclone.data.moldel.Message
import com.example.whatsappclone.data.moldel.MessageType
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextFieldChatAndAdjacentButtons(
    chatScreenViewModel: ChatScreenViewModel,
) {
    val textState = rememberTextFieldState()

    val showButtonSend = remember {
        mutableStateOf(false)
    }

    val gifOrImageMessage = remember {
        mutableStateOf(Message())
    }

    val uriMessage = remember {
        mutableStateOf("")
    }

    val sendGifOrImage = remember {
        mutableStateOf(false)
    }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,

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
        BasicTextField2(
            modifier = Modifier
                .width(240.dp)
                .height(42.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(colorGreyChat)
                .padding(12.dp)
                .receiveContent(MediaType.Image) { content ->
                    uriMessage.value = content.platformTransferableContent?.linkUri.toString()
                    gifOrImageMessage.value = Message(
                        uriImage = uriMessage.value,
                        type = MessageType.IMAGE,
                        user = chatScreenViewModel.userLogPhoneAccount,
                        content = "Image"
                    )
                    sendGifOrImage.value = true
                    null
                },
            state = textState,
            lineLimits = TextFieldLineLimits.SingleLine,
            decorator = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(Modifier.weight(1f)) {
                        if (textState.text.isEmpty()) {
                            Text(
                                text = "Type message...",
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                        showButtonSend.value = textState.text.isNotBlank()

                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.SentimentSatisfied,
                        contentDescription = "Face Icon",
                        tint = GreenButtons
                    )
                }
            }
        )

        if (sendGifOrImage.value) {
            SendGifOrImage(
                chatScreenViewModel = chatScreenViewModel,
                contentMessage = gifOrImageMessage.value
            )
            sendGifOrImage.value = false
        }


        if (!showButtonSend.value) {
            ShowButtonsCameraAndMicrophone()

        } else {
            ShowSendButton(
                chatScreenViewModel = chatScreenViewModel,
                contentMessage = textState.text.toString()
            ) {
                textState.edit {
                    this.replace(0, textState.text.length, it)
                }
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
fun SendGifOrImage(
    chatScreenViewModel: ChatScreenViewModel,
    contentMessage: Message
) {
    println(contentMessage.content)
    println(contentMessage.type)
    val chatExist = remember {
        mutableStateOf(false)
    }
    if (!chatExist.value) {
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
                    Message(
                        content = contentMessage,
                        user = chatScreenViewModel.userLogPhoneAccount
                    ),
                ) { statedFetchChat ->
                    when (statedFetchChat) {
                        ChatScreenViewModel.ChatScreenStated.FoundChat -> {
                            chatScreenViewModel.sendMessage(
                                chatScreenViewModel.userLogPhoneAccount,
                                chatScreenViewModel.numberContact,
                                Message(
                                    user = chatScreenViewModel.userLogPhoneAccount,
                                    content = contentMessage
                                )
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
                    Message(
                        user = chatScreenViewModel.userLogPhoneAccount,
                        content = contentMessage
                    )
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
                    ChatMessageTest1(message = chatList.first().messages[(chatList.first().messages.size) - (it + 1)])
                } else {
                    if (chatScreenViewModel.userLogPhoneAccount == chatList.first().dataUser1.numberPhone.toString()) {
                        ChatTesting2(
                            message = chatList.first().messages[(chatList.first().messages.size) - (it + 1)],
                            chatList.first().dataUser2.userImage
                        )
                    } else {
                        ChatTesting2(
                            message = chatList.first().messages[(chatList.first().messages.size) - (it + 1)],
                            chatList.first().dataUser1.userImage
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageTest1(message: Message) {

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
            if (message.type == MessageType.TEXT) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(10.dp)
                )
            } else {
                AsyncImage(
                    model = message.uriImage,
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                )
            }
        }
    }
}

@Composable
fun ChatTesting2(message: Message, imageUrl: String) {
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
            if (message.type == MessageType.TEXT) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(10.dp)
                )
            } else {
                AsyncImage(
                    model = message.uriImage,
                    contentDescription = null,
                    modifier = Modifier.width(180.dp),
                    contentScale = ContentScale.FillHeight
                )
            }
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

