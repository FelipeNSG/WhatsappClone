package com.example.whatsappclone.screeens.homeScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.whatsappclone.components.AppBarHomeScreen
import com.example.whatsappclone.components.UserDetails
import com.example.whatsappclone.components.UserImage
import com.example.whatsappclone.data.moldel.ChatBoxObject
import com.example.whatsappclone.ui.theme.GreenButtons
import com.example.whatsappclone.ui.theme.GreenWhatsapp
import com.example.whatsappclone.ui.theme.colorGreyChat

typealias CallbackNavControllerNavigationToChatScreen = (String) -> Unit
typealias CallbackNavControllerNavigationToLoginScreen = () -> Unit
typealias CallbackRemoveSession = () -> Unit

@Composable
fun HomeScreen(
    homeViewModel
    : HomeViewModel,
    callbackNavController: CallbackNavControllerNavigationToChatScreen,
    callbackNavControllerNavigationToLoginScreen: CallbackNavControllerNavigationToLoginScreen
) {
    LaunchedEffect(Unit) {
        homeViewModel.sendDataToDataStore()
    }
    val chatList by homeViewModel.getChatList().collectAsStateWithLifecycle(emptyList())
    Scaffold(
        topBar = {
            AppBarHomeScreen(
                homeViewModel.logUser,
                callbackNavControllerNavigationToLoginScreen
            )
            {
                homeViewModel.logOutFirebaseAuth()
                homeViewModel.removeSessionToDataStore()
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                ChatsHomeScreen(
                    homeViewModel,
                    chatList.sortedBy { it.messages.last().timeStamp },
                    homeViewModel.logUser,
                    callbackNavController
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 25.dp, bottom = 40.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            AddContactButton(callbackNavController, homeViewModel)
        }
    }
}

@Composable
fun SearchBarHomeScreen(
    viewModel: HomeViewModel,
    chatList: List<ChatBoxObject>,
    callbackNavController: CallbackNavControllerNavigationToChatScreen,
    callbackText: (TextFieldValue) -> Unit
) {
    val textInput = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            value = textInput.value,
            onValueChange = { newText ->
                textInput.value = newText
                callbackText(textInput.value)
            },
            placeholder = {
                Text(text = "Search...")
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Icon",
                    tint = Color.Gray,
                    modifier = Modifier.clickable {
                        if (textInput.value.text.length >= 9) {
                            viewModel.verify(
                                logUser = viewModel.logUser,
                                numberPhoneContact = textInput.value.text
                            ) { statedResult ->
                                when (statedResult) {
                                    HomeViewModel.HomeScreenStated.CorrectNumber -> {
                                        callbackNavController.invoke(
                                            "/${textInput.value.text}/${viewModel.logUser}/${viewModel.logUser}/noIdDocument"
                                        )
                                    }

                                    HomeViewModel.HomeScreenStated.TheChatAlreadyExist -> {
                                        val chatItem = chatList.filter {
                                            ((it.dataUser1.numberPhone.toString() == viewModel.logUser ||
                                                    it.dataUser2.numberPhone.toString() == viewModel.logUser)
                                                    &&
                                                    (it.dataUser1.numberPhone.toString() == textInput.value.text ||
                                                            it.dataUser2.numberPhone.toString() == textInput.value.text))
                                        }
                                    }

                                    else -> {
                                        textInput.value = TextFieldValue("")
                                    }
                                }
                            }
                        }
                    },
                )
            },
            shape = CircleShape,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = colorGreyChat,
                unfocusedContainerColor = colorGreyChat

            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    Unit
                }
            )
        )
    }
}

@Composable
fun ProfileStatus() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(10) { _ ->
            Card(
                modifier = Modifier
                    .size(60.dp),
                shape = CircleShape,
                border = BorderStroke(2.dp, GreenWhatsapp)

            ) {
                SubcomposeAsyncImage(
                    model = "https://picsum.photos/id/235/200/300",
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
fun ChatsHomeScreen(
    viewModel: HomeViewModel,
    chatList: List<ChatBoxObject>,
    logUser: String,
    callbackNavController: CallbackNavControllerNavigationToChatScreen
) {
    var searchContactInChatList = chatList
    var searchedText = remember {
        mutableStateOf(TextFieldValue(""))
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            SearchBarHomeScreen(
                viewModel = viewModel,
                chatList,
                callbackNavController
            ) { searchedText.value = it }
        }
        item {
            ProfileStatus()
        }
        if (searchContactInChatList.isNotEmpty()) {
            items(searchContactInChatList.size) { chatData ->
                if ((searchContactInChatList[chatData].dataUser1.numberPhone.toString() != logUser) &&
                    (searchContactInChatList[chatData].dataUser1.numberPhone.toString()
                        .startsWith(
                            searchedText.value.text,
                            ignoreCase = true
                        ) || searchContactInChatList[chatData].dataUser1
                        .userAlias.startsWith(searchedText.value.text, ignoreCase = true))
                ) {
                    ChatListItem(
                        searchContactInChatList[chatList.size - (chatData + 1)],
                        logUser,
                        callbackNavController
                    )
                } else {
                    if (searchContactInChatList[chatData].dataUser2.numberPhone.toString()
                            .startsWith(
                                searchedText.value.text,
                                ignoreCase = true
                            ) || searchContactInChatList[chatData].dataUser2
                            .userAlias.startsWith(searchedText.value.text, ignoreCase = true)
                    ) {
                        ChatListItem(
                            searchContactInChatList[chatList.size - (chatData + 1)],
                            logUser,
                            callbackNavController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatListItem(
    chatData: ChatBoxObject,
    logUser: String,
    callbackNavController: CallbackNavControllerNavigationToChatScreen
) {
    val contactName = remember {
        mutableStateOf("")
    }
    val contactNumber = remember {
        mutableStateOf("")
    }

    if (logUser.toLong() != chatData.dataUser1.numberPhone) {
        contactName.value = chatData.dataUser1.userAlias
        contactNumber.value = chatData.dataUser1.numberPhone.toString()
    } else {
        contactName.value = chatData.dataUser2.userAlias
        contactNumber.value = chatData.dataUser2.numberPhone.toString()
    }

    Row(
        modifier = Modifier.clickable {
            callbackNavController.invoke(
                "/${contactNumber.value}/${contactName.value}/${logUser}/${chatData.chatId}"
            )
        }
    ) {
        UserImage(
            chatData,
            logUser,
        )
        UserDetails(
            chatData,
            logUser
        )
    }
}

@Composable
fun AddContactButton(
    callbackNavController: CallbackNavControllerNavigationToChatScreen,
    viewModel: HomeViewModel,
) {
    val openDialog = remember {
        mutableStateOf(false)
    }
    Row(
        horizontalArrangement = Arrangement.End,
    )
    {
        IconButton(
            onClick = { openDialog.value = true },
            modifier = Modifier
                .clip(CircleShape)
                .background(GreenWhatsapp)
                .size(50.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "content description", tint = Color.White)
        }
    }

    if (openDialog.value) {
        OnClickContactAddButton(
            callbackNavController = callbackNavController,
            viewModel = viewModel,
        ) {
            openDialog.value = it
        }
    }
}

@Composable
fun OnClickContactAddButton(
    callbackNavController: CallbackNavControllerNavigationToChatScreen,
    viewModel: HomeViewModel,
    openDialogCallBack: (Boolean) -> Unit
) {
    val contactToAdd = remember {
        mutableStateOf("")
    }
    val name = remember {
        mutableStateOf("")
    }
    var textOfNote by remember { mutableStateOf("") }

    val openDialog = remember {
        mutableStateOf(true)
    }
    if (openDialog.value) {

        AlertDialog(
            containerColor = Color.White,
            textContentColor = Color.White,
            onDismissRequest = {
                openDialog.value = false
                openDialogCallBack(openDialog.value)
                textOfNote = ""
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Add Contact")
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(

                        value = contactToAdd.value,
                        onValueChange = {
                            contactToAdd.value = it
                        },
                        placeholder = {
                            Text(text = "Enter the number phone")
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = GreenButtons,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                    TextField(
                        value = name.value,
                        onValueChange = {
                            name.value = it
                        },
                        placeholder = {
                            Text(text = "Name")
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = GreenButtons,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        openDialogCallBack(openDialog.value)
                        textOfNote = ""
                        viewModel.verify(
                            logUser = viewModel.logUser,
                            numberPhoneContact = contactToAdd.value
                        ) { statedResult ->
                            when (statedResult) {
                                HomeViewModel.HomeScreenStated.CorrectNumber -> {
                                    callbackNavController.invoke(
                                        "/${contactToAdd.value}/${name.value}/${viewModel.logUser}/noIdDocument"
                                    )
                                }

                                else -> {
                                    openDialog.value = false
                                    openDialogCallBack(openDialog.value)
                                    textOfNote = ""
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(GreenWhatsapp)
                ) {
                    Text(text = "APPLY")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        openDialogCallBack(openDialog.value)
                        textOfNote = ""
                    },
                    colors = ButtonDefaults.buttonColors(GreenWhatsapp)
                ) {
                    Text(text = "CANCEL")
                }
            },
        )
    }
}

