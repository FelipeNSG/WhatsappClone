package com.example.whatsappclone.data

import android.net.Uri
import com.example.whatsappclone.utils.Constants
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class FirebaseStorageManager: ImageRepository {
    private val storage = Firebase.storage
    override suspend fun addImageToFirebaseStorage(imageUri: Uri): AddImageToStorageResponse {
        return try {
            val downloadUrl = storage.reference.child(Constants.IMAGES).child(Constants.IMAGE_NAME)
                .putFile(imageUri).await()
                .storage.downloadUrl.await()
            Response.Success(downloadUrl)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}

sealed class Response<out T>{
    object  Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T?
    ): Response<T>()

    data class  Failure(
        val e: Exception
    ): Response<Nothing>()
}