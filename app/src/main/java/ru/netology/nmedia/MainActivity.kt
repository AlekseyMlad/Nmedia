package ru.netology.nmedia

import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                contentTextView.text = post.content
                likeImageView.setImageResource(if (post.likedByMi) R.drawable.ic_likes_red else R.drawable.ic_likes)
                likeCountTextView.text = viewModel.formatCount(post.likes, this@MainActivity)
                shareCountTextView.text = viewModel.formatCount(post.shares, this@MainActivity)
                viewCountTextView.text = viewModel.formatCount(post.views, this@MainActivity)
            }
        }

        binding.likeImageView.setOnClickListener {
            viewModel.like()
        }

        binding.shareImageView.setOnClickListener {
            viewModel.share()
        }

//        binding.root.setOnClickListener {
//            viewModel.view()
//        }
    }
}