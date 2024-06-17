package com.example.dali_bike

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dali_bike.model.viewCategoryPost
import com.example.dali_bike.models.MyPost

class PostListAdapter(val context: Context, var list: MutableList<viewCategoryPost>) :
    RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.postlist, parent, false)
        return PostsAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsAdapter.MyViewHolder, position: Int) {
        val post = list[position]
        val titleMax = 10
        val contentMax = 15

        holder.title.text = post.Title.truncateWithEllipsis(titleMax)
        holder.content.text = post.Content.truncateWithEllipsis(contentMax)
        holder.like.text = post.Like.toString()
        holder.comment.text = post.CommentCount.toString()

        holder.showBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("postId", post.PostId.toString())
            }
            holder.itemView.findNavController().navigate(R.id.action_postFragment_to_postDetailFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}