package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val videoUrl: String? = null
) {
    fun toDto() = Post(id, author, published, content, likedByMe, likes, shares, views, videoUrl)

    companion object {
        fun fromDto(post: Post) = PostEntity(
            post.id,
            post.author,
            post.published,
            post.content,
            post.likedByMe,
            post.likes,
            post.shares,
            post.views,
            post.videoUrl
        )
    }
}

