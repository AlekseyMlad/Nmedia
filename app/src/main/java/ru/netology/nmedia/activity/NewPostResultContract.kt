package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

object NewPostResultContract: ActivityResultContract<Intent?, PostResult?>() {

    override fun createIntent(context: Context, input: Intent?): Intent {
        val intent = Intent(context, NewPostActivity::class.java)
        input?.let {
            intent.putExtra("post_id", it.getLongExtra("post_id", 0L))
            intent.putExtra("post_content", it.getStringExtra("post_content"))
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): PostResult? {
        if (resultCode != android.app.Activity.RESULT_OK){
            return null
        }
        return PostResult(
            intent?.getLongExtra("post_id",0L) ?: 0L,
            intent?.getStringExtra(Intent.EXTRA_TEXT) ?: " "
        )
    }

}

data class PostResult(val id: Long, val content: String)