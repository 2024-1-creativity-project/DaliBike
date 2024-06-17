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
import com.example.dali_bike.models.MyPost

class PostsAdapter(val context: Context, var list: MutableList<MyPost>) :
    RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.postlist, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = list[position]
        val titleMax = 10
        val contentMax = 15

        holder.title.text = post.Title.truncateWithEllipsis(titleMax)
        holder.content.text = post.Content.truncateWithEllipsis(contentMax)
        holder.like.text = post.Like.toString()

        holder.showBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("postId", post.PostId.toString())
            }
            holder.itemView.findNavController().navigate(R.id.action_myPostFragment_to_postDetailFragment, bundle)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.postTitle)
        var content: TextView = view.findViewById(R.id.postContent)
        var like: TextView = view.findViewById(R.id.postLike)
        var showBtn: ImageButton = view.findViewById(R.id.show_btn)
        var comment: TextView = view.findViewById(R.id.postComment)
    }
}
