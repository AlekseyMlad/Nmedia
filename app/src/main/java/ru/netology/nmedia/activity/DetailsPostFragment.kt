package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.databinding.FragmentDetailsBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.adapter.PostViewHolder


class DetailsPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private var postId: Long = 0

    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        postId = arguments?.getLong("postId") ?: throw IllegalArgumentException("Post ID is required")

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe


            val viewHolder = PostViewHolder(binding.post, object : OnInteractionListener {
                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
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

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                    findNavController().popBackStack()
                }

                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    findNavController().navigate(
                        R.id.action_detailsPostFragment_to_newPostFragment,
                        Bundle().apply { textArg = post.content }
                    )
                }

                override fun onPostClick(post: Post) {

                }
            })
            viewHolder.bind(post)

            //видео если есть
            if (post.videoUrl != null) {
                binding.post.videoGroup.visibility = View.VISIBLE

                binding.post.videoPlay.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                    startActivity(intent)
                }
            } else {
                binding.post.videoPreview.visibility = View.GONE
                binding.post.videoPlay.visibility = View.GONE
            }

        }

        return binding.root
    }
}