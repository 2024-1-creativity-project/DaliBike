package com.example.dali_bike

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dali_bike.models.PostList



// TODO: Rename parameter arguments, choose names that match

class PostListFragment (val context: Context, var list: MutableList<PostList>) :
    RecyclerView.Adapter<PostListFragment.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        //val view: View = inflater.inflate(R.layout.user_row,parent,false)
        val view: View = inflater.inflate(R.layout.fragment_post_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val postList = list.get(position)
        //holder.name?.text = user.avatar
        holder.title?.text = postList?.Title
        holder.content?.text = postList.Content
        holder.like?.text = postList?.Like.toString()
        holder.comment?.text = postList?.CommentCount.toString()

//        val imgUri = postList.avatar.toUri().buildUpon().scheme("https").build()
//        holder.title?.let {
//            Glide.with(context)
//                .load(imgUri)
//                .into(it)
//        }

    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var name: ImageView? = null
        //var name: ImageView? = null
        var title: TextView? = null
        var content: TextView? = null
        var like: TextView? = null
        var comment: TextView? = null

        init {
            //name = view.findViewById(R.id.txt_avatar)
            title = view.findViewById(R.id.post_title)
            content = view.findViewById(R.id.post_content)
            like = view.findViewById(R.id.post_like)
//            comment = view.findViewById(R.id.commentCount1)
        }

    }

}

