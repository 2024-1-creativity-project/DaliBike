package com.example.dali_bike

import UserViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dali_bike.api.apiService
import com.example.dali_bike.models.MyPost
import kotlinx.coroutines.launch
import retrofit2.Response

class MyPostFragment : Fragment() {

    private lateinit var recycler_main: RecyclerView
    private var listPost: MutableList<MyPost> = mutableListOf()
    private var adapter: PostsAdapter? = null
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_post, container, false)
        recycler_main = view.findViewById(R.id.recyclerView)
        adapter = PostsAdapter(requireContext(), listPost)
        recycler_main.adapter = adapter

        // Get post list after initializing the RecyclerView
        getPostList()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapBtn : AppCompatImageButton = view.findViewById(R.id.mapBtn)
        val postBtn : AppCompatImageButton = view.findViewById(R.id.postBtn)
        val mainBtn : AppCompatImageButton = view.findViewById(R.id.homeBtn)
        val myPageBtn : AppCompatImageButton = view.findViewById(R.id.myPageBtn)

        mapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myPostFragment_to_naverMapFragment)
        }

        postBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myPostFragment_to_postFragment)
        }

        mainBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myPostFragment_to_mainFragment)
        }

        myPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myPostFragment_to_myPageFragment)
        }
    }

    private fun getPostList() {
        lifecycleScope.launch {
            try {
                val id = userViewModel.user.value?.userId
                val response: Response<List<MyPost>> = apiService.myPost(id.toString())

                if (response.isSuccessful) {
                    response.body()?.let { postList ->
                        listPost.clear()
                        listPost.addAll(postList)
                        adapter?.notifyDataSetChanged()
                        Log.d("MyPostFragment", "Posts loaded: ${postList.size}")
                    }
                } else {
                    Log.e("Error", "Failed to fetch posts: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage)
            }
        }
    }
}
