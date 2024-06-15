package com.example.dali_bike

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        // Log the data being bound to the ViewHolder
        Log.d("PostsAdapter", "Binding post at position $position: Title=${post.Title}, Content=${post.Content}, Like=${post.Like}")

        holder.title.text = post.Title
        holder.content.text = post.Content
        holder.like.text = post.Like.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }


}