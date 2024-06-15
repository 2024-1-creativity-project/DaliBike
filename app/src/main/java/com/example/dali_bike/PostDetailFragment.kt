package com.example.dali_bike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_detail, container, false)

        val postId = arguments?.getString("postId").toString()

        val title_txt: TextView = view.findViewById(R.id.title_txt)
        val content_txt: TextView = view.findViewById(R.id.content_txt)
        val post_nick: TextView = view.findViewById(R.id.post_nick_txt)
        val like_num: TextView = view.findViewById(R.id.like_num)
        val comment_num: TextView = view.findViewById(R.id.comment_num)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.postInfo(postId)

                if (response.isSuccessful) {
                    val responList = response.body()

                    withContext(Dispatchers.Main) {
                        if (responList != null) {
                            val main = responList[0]

                            title_txt.text = main.Title
                            content_txt.text = main.Content
                            post_nick.text = main.NickName
                            if (main.Like >= 1000) {
                                like_num.text = "+999"
                            }
                            else {
                                like_num.text = main.Like.toString()
                            }
                            if (main.comments.size >= 1000) {
                                comment_num.text = "+999"
                            }
                            else {
                                comment_num.text = main.comments.size.toString()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapBtn: AppCompatImageButton = view.findViewById(R.id.mapBtn)
        val mainBtn: AppCompatImageButton = view.findViewById(R.id.homeBtn)
        val postBtn: AppCompatImageButton = view.findViewById(R.id.postBtn)
        val myPageBtn: AppCompatImageButton = view.findViewById(R.id.myPageBtn)

        mapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_postDetailFragment_to_myPageFragment)
        }

        mainBtn.setOnClickListener {
            findNavController().navigate(R.id.action_postDetailFragment_to_mainFragment)
        }

        postBtn.setOnClickListener {
            findNavController().navigate(R.id.action_postDetailFragment_to_postFragment)
        }

        myPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_postDetailFragment_to_myPageFragment)
        }
    }
}