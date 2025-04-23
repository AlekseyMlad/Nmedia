package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId: Long = 1

    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по" +
                    " онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и " +
                    "управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных" +
                    " профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила," +
                    " которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь" +
                    " встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likedByMi = false,
            likes = 9999,
            shares = 1999,
            views = 500099
        ),

        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "20 июля в 10:28",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по" +
                    " онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и " +
                    "управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных",
            likedByMi = false,
            likes = 119999,
            shares = 4099,
            views = 999
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "20 июля в 10:28",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по  онлайн-маркетингу.",
            likedByMi = false,
            likes = 559999,
            shares = 4899,
            views = 9999999
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "20 июля в 10:28",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по" +
            " онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и " +
            "управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных" +
            " -> https://youtu.be/BT38K6NqETE?si=lAbr5ohe9ehM0VAU",
            likedByMi = false,
            likes = 1199990,
            shares = 40099,
            views = 9099,
            videoUrl = "https://youtu.be/BT38K6NqETE?si=lAbr5ohe9ehM0VAU"
        )
    ).reversed()

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMi = !it.likedByMi,
                likes = if (it.likedByMi) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L){
            listOf(post.copy(id = nextId++, author = "Me")) + posts
        }else{
            posts.map {
                if (it.id == post.id) {
                    post
                } else {
                    it
                }
            }
        }
        data.value = posts
    }
}