package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.dto.Post


class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    private val newPostLauncher =
        registerForActivityResult(NewPostResultContract) { result ->
            if (result == null) {
                return@registerForActivityResult
            }
            if (result.id == 0L) {

                viewModel.changeContentAndSave(result.content)
            } else {
                val postToEdit = viewModel.data.value?.find { it.id == result.id }
                if (postToEdit != null) {

                    viewModel.edit(postToEdit.copy(content = result.content))
                    viewModel.changeContentAndSave(result.content)
                }
            }
        }

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
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val sharedIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(sharedIntent)
            }

            override fun onEdit(post: Post) {
                val intent = Intent(this@MainActivity, NewPostActivity::class.java).apply {
                    putExtra("post_id", post.id)
                    putExtra("post_content", post.content)
                }
                newPostLauncher.launch(intent)
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

        binding.fab.setOnClickListener{
            newPostLauncher.launch(intent)
        }
    }
}
