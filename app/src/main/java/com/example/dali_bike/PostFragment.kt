package com.example.dali_bike

import UserViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.dali_bike.databinding.FragmentPostBinding
import com.example.dali_bike.models.MyPost
import kotlinx.coroutines.launch
import java.lang.Exception
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import retrofit2.Response

class PostFragment : Fragment() {
    private lateinit var _binding: FragmentPostBinding
    private lateinit var recycler_main: RecyclerView
    private var listPost: MutableList<MyPost> = mutableListOf()
    private var adapter: PostsAdapter? = null
    private val userViewModel: UserViewModel by activityViewModels()

    private var selectedCategory: String? = null // 선택한 카테고리를 저장할 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // view binding을 사용하여 레이아웃 인플레이트
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val rootView = _binding.root

        // rootView를 사용하여 recycler_main 초기화
        recycler_main = rootView.findViewById(R.id.recyclerView)
        adapter = PostsAdapter(requireContext(), listPost)
        recycler_main.adapter = adapter

        getPostList()

        return rootView
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.view_post_category,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            _binding.categorySpinner.adapter = adapter
        }

        _binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    if (p0 != null) {
                        // 선택한 항목의 위치를 가져옴
                        val selectedItemPosition = p2
                        // 선택한 항목의 값을 가져옴
                        val selectedItemValue = p0.getItemAtPosition(p2).toString()

                        // 선택한 항목을 변수에 저장
                        selectedCategory = selectedItemValue
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // 아무것도 하지 않음
                }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSpinner()
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

    companion object {
        private fun getPostList(postFragment: PostFragment) {
            postFragment.lifecycleScope.launch {
                try {
                    val id = postFragment.userViewModel.user.value?.userId
                    val response: Response<List<MyPost>> = apiService.myPost(id.toString())

                    if (response.isSuccessful) {
                        response.body()?.let { postList ->
                            postFragment.listPost.clear()
                            postFragment.listPost.addAll(postList)
                            postFragment.adapter?.notifyDataSetChanged()
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
}
