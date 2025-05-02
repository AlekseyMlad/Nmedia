package ru.netology.nmedia.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import kotlin.random.Random

class FCMService: FirebaseMessagingService() {


    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun fromString(value: String?): Action? {
        return when (value) {
            "LIKE" -> Action.LIKE
            "NEW_POST" -> Action.NEW_POST
            else -> null
        }
    }


    override fun onMessageReceived(message: RemoteMessage) {

        message.data["action"]?.let {
            if (fromString(message.data["action"]) != null){
                when (Action.valueOf(it)) {
                    Action.LIKE -> handleLike(Gson().fromJson(message.data["content"], Like::class.java))
                    Action.NEW_POST -> handleNewPost(Gson().fromJson(message.data["content"], Post::class.java))
                }

            }else {
                -> handleError()
            }
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }


    private fun handleError() {
        Toast.makeText(this, getString(R.string.error_empty_content), Toast.LENGTH_LONG).show()
    }

    private fun handleNewPost(post: Post) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(this.getString(R.string.new_post_from, post.author))
                .setContentText(post.content)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(post.content)
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            NotificationManagerCompat.from(this).notify(Random.nextInt(100_000), notification)
        }

    }

    private fun handleLike(like: Like) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.notification_user_liked, like.userName, like.postAuthor))
                .build()

            NotificationManagerCompat.from(this).notify(Random.nextInt(100_000), notification)
        }

    }

    companion object {
        private const val CHANNEL_ID = "notification"


    }

}

enum class Action {
    LIKE,
    NEW_POST
}

data class Like(
    val userId: Int,
    val userName: String,
    val postId: Int,
    val postAuthor: String
)