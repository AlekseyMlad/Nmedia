package ru.netology.nmedia.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.formatCount


interface OnInteractionListener {
    fun onLike(post: Post)
    fun onRemove(post: Post)
    fun onShare(post: Post)
    fun onEdit(post: Post)
    fun onPostClick(post: Post)
}


class PostsAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
        holder.itemView.tag = post.id
        if (post.videoUrl != null) {     //видео если есть
            holder.binding.videoPreview.visibility = View.VISIBLE
            holder.binding.videoPlay.visibility = View.VISIBLE
            holder.binding.videoPlay.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                holder.itemView.context.startActivity(intent)
            }
        } else {
            holder.binding.videoPreview.visibility = View.GONE
            holder.binding.videoPlay.visibility = View.GONE
            holder.binding.videoPlay.setOnClickListener(null)
        }

    }
}


class PostViewHolder(
    val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) = with(binding) {
        author.text = post.author
        published.text = post.published
        contentTextView.text = post.content
        likeImageView.apply {
            isChecked = post.likedByMi
            text = post.likes.formatCount()
        }
        shareImageView.text = post.shares.formatCount()
        viewIcon.text = post.views.formatCount()

        likeImageView.setOnClickListener {
            onInteractionListener.onLike(post)
        }

        shareImageView.setOnClickListener {
            onInteractionListener.onShare(post)
        }

        menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.post_actions)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            onInteractionListener.onRemove(post)
                            true
                        }

                        R.id.edit -> {
                            onInteractionListener.onEdit(post)
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }
        root.setOnClickListener {
            onInteractionListener.onPostClick(post)
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

}