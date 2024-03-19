package com.example.whatsappclone.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.whatsappclone.ui.theme.GreenWhatsapp
import com.example.whatsappclone.ui.theme.fontFamilyMonserrat


@Composable
fun TopAppBarChatScreen() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Icon ArrowBack"
            )
        }
        Box {
            Card(
                modifier = Modifier
                    .size(40.dp),
                shape = CircleShape,
            ) {
                SubcomposeAsyncImage(
                    model = "https://picsum.photos/id/235/200/300",
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = "Circle Icon",
                tint = GreenWhatsapp,
                modifier = Modifier
                    .padding(2.dp)
                    .size(10.dp)
                    .align(Alignment.BottomEnd)
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = CircleShape
                    )

            )
        }

        Column(
            modifier = Modifier.padding(start = 15.dp)
        ) {
            Text(
                text = "Adholk Abdul",
                fontFamily = fontFamilyMonserrat(500),
                fontSize = 14.sp,
                color = Color.Black,
            )
            Text(
                text = "Active Now",
                fontSize = 11.sp,
                color = Color.Gray,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            IconButton(
                modifier = Modifier.size(40.dp),
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Filled.Videocam,
                    contentDescription = "Video Cam",
                    tint = Color.Gray,
                )
            }

            IconButton(
                modifier = Modifier.size(40.dp),
                onClick = { },
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "Call",
                    tint = Color.Gray
                )
            }

            IconButton(
                modifier = Modifier.size(40.dp),
                onClick = {},
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More Vert",
                    tint = Color.Gray
                )
            }
        }
    }
}