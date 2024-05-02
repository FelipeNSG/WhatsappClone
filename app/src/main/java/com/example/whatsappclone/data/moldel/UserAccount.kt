package com.example.whatsappclone.data.moldel

data class UserAccount(
    var id:String? = null,
    val numberPhone: Long = 0,
    var userName:String = "",
    val userImage: String = imageRandom(),
    val userId:String? = null
)

fun imageRandom(): String {
    val number = (230..250).random()
    return "https://picsum.photos/id/$number/200/300"
}