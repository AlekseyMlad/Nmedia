package ru.netology.nmedia

import android.content.Context
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel: ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()

    fun like() = repository.like()
    fun share() = repository.share()
    fun view() = repository.view()

    fun formatCount(count: Int, context: Context): String {
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