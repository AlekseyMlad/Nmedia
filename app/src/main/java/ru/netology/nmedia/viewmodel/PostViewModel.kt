package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun viewById(id: Long) = repository.viewById(id)

    fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000 -> {
                val million = count / 100_000
                (million / 10.0).toString() + "M"
            }

            count >= 10_000 -> (count / 1000).toString() + "K"
            count >= 1_000 -> {
                val thousand = count / 100
                (thousand / 10.0).toString() + "K"
            }

            else -> count.toString()
        }
    }

}