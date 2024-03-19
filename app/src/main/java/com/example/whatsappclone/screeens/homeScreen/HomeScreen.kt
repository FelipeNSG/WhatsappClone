package com.example.whatsappclone.screeens.homeScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.whatsappclone.components.AppBarHomeScreen
import com.example.whatsappclone.components.UserDetails
import com.example.whatsappclone.components.UserImage
import com.example.whatsappclone.domain.ChatListDataObject
import com.example.whatsappclone.ui.theme.GreenWhatsapp
import com.example.whatsappclone.utils.FireStoreManager


//TODO(REMOVE navController FROM HomeScreen AND all the others functions)
//TODO(USE OF CALLBACKS TO COMUNICATE INNER COMPOSABLE'S WITH HomeScreen)
//TODO(SEPARATE LOGIC FROM BUTTON ADD)

@Composable
fun HomeScreen(navController: NavController, fireStoreManager: FireStoreManager) {
    Scaffold(
        topBar = { AppBarHomeScreen() },
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
                ChatsHomeScreen()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 25.dp, bottom = 40.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            ButtonAdd(navController, fireStoreManager)
        }
    }
}


@Composable
fun SearchBarHomeScreen() {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        value = "",
        onValueChange = {},
        placeholder = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "Search...")
            }
        },
        shape = CircleShape,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
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

@Preview
@Composable
fun ChatsHomeScreen() {
    val notes = userList.collectAsState(
        emptyList()
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            SearchBarHomeScreen()
        }
        item {
            ProfileStatus()
        }
        items(notes.value) { chatData ->
            ChatListItem(chatData)
        }

    }
}

@Composable
fun ChatListItem(chatData: ChatListDataObject) {
    Row(

    ) {
        UserImage(chatData.userImage)
        UserDetails(chatData)
    }
}

@Composable
fun ButtonAdd(navController: NavController, fireStoreManager: FireStoreManager) {
    val userConversation = remember {
        mutableStateOf("")
    }
    val name = remember {
        mutableStateOf("")
    }
    var textOfNote by remember { mutableStateOf("") }

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
        val scope = rememberCoroutineScope()
        AlertDialog(

            onDismissRequest = {
                openDialog.value = false
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
                        value = userConversation.value,
                        onValueChange = {
                            userConversation.value = it
                        },
                        placeholder = {
                            Text(text = "Enter the number phone")
                        }
                    )
                    TextField(
                        value = name.value,
                        onValueChange = {
                            name.value = it
                        },
                        placeholder = {
                            Text(text = "Name")
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        textOfNote = ""
                       fireStoreManager.consultUser(navController, userConversation.value, name.value)

                        /*updateFlow()*/
                    },

                    ) {
                    Text(text = "APPLY")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        textOfNote = ""
                    },

                    ) {
                    Text(text = "CANCEL")
                }
            },
        )
    }

}

