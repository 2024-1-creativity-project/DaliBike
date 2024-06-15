package com.example.dali_bike

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        // Log the data being bound to the ViewHolder
        Log.d("PostsAdapter", "Binding post at position $position: Title=${post.Title}, Content=${post.Content}, Like=${post.Like}")

        holder.title.text = post.Title
        holder.content.text = post.Content
        holder.like.text = post.Like.toString()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.post_title)
        var content: TextView = view.findViewById(R.id.post_content)
        var like: TextView = view.findViewById(R.id.post_like)
    }
}
