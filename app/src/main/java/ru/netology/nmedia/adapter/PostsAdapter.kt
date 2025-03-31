package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

typealias OnLikeListener = (Post) -> Unit
typealias OnShareListener = (Post) -> Unit
typealias OnViewListener = (Post) -> Unit


class PostsAdapter(private val onLikeListener: OnLikeListener,
                   private val onShareListener: OnShareListener,
                   private val onViewListener: OnViewListener,
                   private val viewModel: PostViewModel
    ) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val view = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PostViewHolder(view, onLikeListener, onShareListener, onViewListener, viewModel)
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            holder.bind(getItem(position))
        }


    }


class PostViewHolder(private val binding: CardPostBinding,
                     private val onLikeListener: OnLikeListener,
                     private val onShareListener: OnShareListener,
                     private val onViewListener: OnViewListener,
                     private val viewModel: PostViewModel
    ) : RecyclerView.ViewHolder(binding.root){
    fun bind(post: Post) = with(binding) {
        author.text = post.author
        published.text = post.published
        contentTextView.text = post.content
        likeImageView.setImageResource(
            if (post.likedByMi) R.drawable.ic_likes_red else R.drawable.ic_likes
        )
        likeCountTextView.text = viewModel.formatCount(post.likes)
        shareCountTextView.text = viewModel.formatCount(post.shares)
        viewCountTextView.text = viewModel.formatCount(post.views)

        likeImageView.setOnClickListener{
            onLikeListener(post)
        }

        shareImageView.setOnClickListener {
            onShareListener(post)
        }

        root.setOnClickListener {
            onViewListener(post)
        }
    }

}

object PostDiffCallback : DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

}