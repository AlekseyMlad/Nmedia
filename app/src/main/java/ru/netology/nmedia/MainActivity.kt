package ru.netology.nmedia

import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.dto.Post
import android.content.Context


class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding
    private lateinit var post: Post
    private var shareCount = 9148
    private var viewCount = 5500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            like = 1099,
            likedByMi = false
        )

        updateLikeButton()
        updateCounts()

        binding.likeImageView.setOnClickListener {
            post.likedByMi = !post.likedByMi
            if (post.likedByMi) {
                post.like++
            } else {
                post.like--
            }
            updateLikeButton()
            updateCounts()
        }

        binding.shareImageView.setOnClickListener {
            shareCount++
            updateCounts()
        }
    }


    private fun updateLikeButton() {
        binding.likeImageView.setImageResource(if (post.likedByMi) R.drawable.ic_like_red_24 else R.drawable.ic_like_24)
    }

    private fun updateCounts() {
        binding.likeCountTextView.text = formatCount(post.like, this)
        binding.shareCountTextView.text = formatCount(shareCount, this)
        binding.viewCountTextView.text = formatCount(viewCount, this)
    }


    private fun formatCount(count: Int, context: Context): String {
        return when {
            count >= 1000000 -> String.format(context.getString(R.string.format_million), count.toDouble() / 1000000)
            count >= 1000 -> String.format(context.getString(R.string.format_thousand), count.toDouble() / 1000)
            else -> count.toString()
        }
    }
}