package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import ru.netology.nmedia.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils



class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    private var editedPostId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onEdit(post: Post) {
                editedPostId = post.id
                viewModel.edit(post)
            }

        })
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.currentList.size < posts.size
            adapter.submitList(posts){
                if (newPost) {
                    binding.list.scrollToPosition(0)
                }
            }
        }

        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {

                binding.content.setText(post.content)
                binding.content.requestFocus()

                binding.editGroup.visibility = View.VISIBLE
            } else {
                binding.content.setText("")
                binding.content.clearFocus()

                binding.editGroup.visibility = View.GONE

            }
        }

        binding.add.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isBlank()) {
                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModel.changeContentAndSave(text)

            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(it)
        }

        binding.editCancel.setOnClickListener {
            viewModel.cancelEdit()
        }


    }

}
