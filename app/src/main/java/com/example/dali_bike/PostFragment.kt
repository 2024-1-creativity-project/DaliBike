package com.example.dali_bike

import androidx.recyclerview.widget.RecyclerView
import UserViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.databinding.FragmentPostBinding
import com.example.dali_bike.model.viewCategoryPost
import com.example.dali_bike.models.MyPost
import kotlinx.coroutines.launch
import retrofit2.Response

class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var recycler_main: RecyclerView
    private var listPost: MutableList<viewCategoryPost> = mutableListOf() // 타입 변경
    private var adapter: PostListAdapter? = null
    private val userViewModel: UserViewModel by activityViewModels()

    private var viewSelectedCategory: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val rootView = binding.root

        recycler_main = rootView.findViewById(R.id.recyclerView)
        adapter = PostListAdapter(requireContext(), listPost)
        recycler_main.adapter = adapter

        val writePost_Btn: ImageButton = binding.writePostBtn
        writePost_Btn.setOnClickListener {
            findNavController().navigate(R.id.action_postFragment_to_writePost)
        }

        initSpinner()
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
            binding.categorySpinner.adapter = adapter
        }

        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {
                    if (p0 != null) {
                        val selectedItemValue = p0.getItemAtPosition(p2).toString()
                        viewSelectedCategory = selectedItemValue
                        binding.postCategory.text = viewSelectedCategory ?: ""
                        getPostList()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun getPostList() {
        lifecycleScope.launch {
            try {
                if (viewSelectedCategory.isNullOrEmpty()) {
                    Log.e("Error", "No category selected")
                    return@launch
                }

                val response: Response<List<viewCategoryPost>> = apiService.viewCategoryPost(viewSelectedCategory.toString())

                if (response.isSuccessful) {
                    response.body()?.let { postList ->
                        listPost.clear()
                        listPost.addAll(postList)
                        adapter?.notifyDataSetChanged()
                        Log.d("PostFragment", "Posts loaded: ${postList.size}")
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
        super.onViewCreated(view, savedInstanceState)

        val mapBtn: AppCompatImageButton = view.findViewById(R.id.mapBtn)
        val postBtn: AppCompatImageButton = view.findViewById(R.id.postBtn)
        val mainBtn: AppCompatImageButton = view.findViewById(R.id.homeBtn)
        val myPageBtn: AppCompatImageButton = view.findViewById(R.id.myPageBtn)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
