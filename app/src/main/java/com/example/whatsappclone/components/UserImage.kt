package com.example.whatsappclone.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun UserImage(userImage:String) {
    Card(
        modifier = Modifier
            .size(60.dp),
        shape = CircleShape,

    ) {
        SubcomposeAsyncImage(
            model = userImage,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}