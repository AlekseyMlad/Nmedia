package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFilesImpl
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
import ru.netology.nmedia.repository.PostRepositorySharedPrefsImpl

private val empty = Post(
    id = 0,
    author = "",
    published = "",
    content = "",
    likedByMi = false
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryFilesImpl(application)
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun edit(post: Post){
        edited.value = post
    }

    fun changeContentAndSave(content: String){
        val text = content.trim()
        edited.value?.let {
            repository.save(it.copy(content = text))
        }
        edited.value = empty
    }


}