package com.example.whatsappclone.data

import android.net.Uri

typealias AddImageToStorageResponse = Response<Uri>

interface ImageRepository {
    suspend fun addImageToFirebaseStorage(imageUri: Uri): AddImageToStorageResponse
}