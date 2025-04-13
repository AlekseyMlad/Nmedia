package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postId = intent.getLongExtra("post_id", 0L)
        val postContent = intent.getStringExtra("post_content")

        if (postContent != null) {
            binding.content.setText(postContent)
        }

        binding.content.requestFocus()
        binding.add.setOnClickListener {
            val content = binding.content.text.toString()
            if (content.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED)
            }else{
                Intent().apply {
                    putExtra("post_id", postId)
                    putExtra(Intent.EXTRA_TEXT, content)
                }.let { intent ->
                    setResult(Activity.RESULT_OK, intent)
                }
            }
            finish()
        }

    }
}