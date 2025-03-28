package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var like: Int = 0,
    var likedByMi: Boolean
)
