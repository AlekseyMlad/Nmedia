package ru.netology.nmedia

import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.util.Log
import android.widget.Toast
import ru.netology.nmedia.dto.Post

//import android.activity.viewModels

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var shareCount =99999
    private var viewCount = 5500000

    //    private val viewModel: PostViewModel by viewModels()
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        published = "21 мая в 18:36",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по" +
                " онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и " +
                "управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных" +
                " профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила," +
                " которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь" +
                " встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        like = 10999,
        likedByMi = false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        viewModel.data.observe(this) { post ->
        updateLikeButton()
        updateCounts()
        binding.author.text = post.author
        binding.published.text = post.published
        binding.contentTextView.text = post.content

        binding.root.setOnClickListener {
            Log.d("MainActivity", "Кликнул не по кнопке")
            Toast.makeText(this, "Кликнул не по кнопке", Toast.LENGTH_SHORT).show()
        }


        binding.likeImageView.setOnClickListener {
            Log.d("MainActivity", "Кликнул лайк")
            Toast.makeText(this, "Кликнул лайк", Toast.LENGTH_SHORT).show()
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

        binding.menu.setOnClickListener {
            Log.d("MainActivity", "Кликнул меню")

            Toast.makeText(this, "Кликнул меню", Toast.LENGTH_SHORT).show()
        }

        binding.avatar.setOnClickListener {
            Log.d("MainActivity", "Кликнул аватар")

            Toast.makeText(this, "Кликнул аватар", Toast.LENGTH_SHORT).show()
        }


    }


    private fun updateLikeButton() {
        binding.likeImageView.setImageResource(if (post.likedByMi) R.drawable.ic_likes_red else R.drawable.ic_likes)
    }

    private fun updateCounts() {
        binding.likeCountTextView.text = formatCount(post.like, this)
        binding.shareCountTextView.text = formatCount(shareCount, this)
        binding.viewCountTextView.text = formatCount(viewCount, this)
    }


    private fun formatCount(count: Int, context: Context): String {
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