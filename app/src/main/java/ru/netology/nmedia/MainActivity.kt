package ru.netology.nmedia

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.dto.Post



class MainActivity : AppCompatActivity() {

    private lateinit var likeImageView: ImageButton
    private lateinit var likeCountTextView: TextView
    private lateinit var shareImageView: ImageButton
    private lateinit var shareCountTextView: TextView
    private lateinit var viewCountTextView: TextView

    private lateinit var post: Post
    private var shareCount = 9148
    private var viewCount = 5500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        likeImageView = findViewById(R.id.likeImageView)
        likeCountTextView = findViewById(R.id.likeCountTextView)
        shareImageView = findViewById(R.id.shareImageView)
        shareCountTextView = findViewById(R.id.shareCountTextView)
        viewCountTextView = findViewById(R.id.viewCountTextView)

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

        likeImageView.setOnClickListener {
            post.likedByMi = !post.likedByMi
            if (post.likedByMi) {
                post.like++
            } else {
                post.like--
            }
            updateLikeButton()
            updateCounts()
        }

        shareImageView.setOnClickListener {
            shareCount++
            updateCounts()
        }
    }


    private fun updateLikeButton() {
        likeImageView.setImageResource(if (post.likedByMi) R.drawable.ic_like_red_24 else R.drawable.ic_like_24)
    }

    private fun updateCounts() {
        likeCountTextView.text = formatCount(post.like)
        shareCountTextView.text = formatCount(shareCount)
        viewCountTextView.text = formatCount(viewCount)
    }


    private fun formatCount(count: Int): String {
        return when {
            count >= 1000000 -> String.format("%.1fM", count.toDouble() / 1000000)
            count >= 10000 -> String.format("%dK", count / 1000)
            count >= 1100 -> String.format("%.1fK", count.toDouble() / 1000)
            count >= 1000 -> "1K"
            else -> count.toString()
        }
    }
}