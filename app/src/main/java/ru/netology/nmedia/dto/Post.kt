package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likes: Int,
    val likedByMi: Boolean,
    val shares: Int = 0,
    val views: Int = 0
)
