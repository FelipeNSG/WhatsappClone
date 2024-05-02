package com.example.whatsappclone.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.whatsappclone.data.moldel.ChatBoxObject

@Composable
fun UserImage(
    chatInformation: ChatBoxObject,
    logUser: String,
) {
    val urlImage = remember {
        mutableStateOf("")
    }
    Card(
        modifier = Modifier
            .size(60.dp),
        shape = CircleShape,
    ) {
        if (logUser != chatInformation.dataUser1.numberPhone.toString()) {
            urlImage.value = chatInformation.dataUser2.userImage
        } else {
            urlImage.value = chatInformation.dataUser1.userImage
        }
        SubcomposeAsyncImage(
            model = urlImage.value,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

    }
}
