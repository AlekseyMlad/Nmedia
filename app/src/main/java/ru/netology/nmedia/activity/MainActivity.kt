package ru.netology.nmedia.activity

import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.adapter.PostsAdapter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(
            { post ->
                viewModel.likeById(post.id)
            },
            { post ->
                viewModel.shareById(post.id)
            },
            { post ->
                viewModel.viewById(post.id)
            },
            viewModel
        )
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}