package com.example.dali_bike

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.dali_bike.api.MyApi
import com.example.dali_bike.databinding.FragmentPostBinding

import com.example.dali_bike.models.PostList
import kotlinx.coroutines.launch
import java.lang.Exception
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.databinding.FragmentWritepostBinding
import com.example.dali_bike.models.WritePost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PostFragment : Fragment() {
    private lateinit var _binding: FragmentPostBinding
    //private lateinit var txtData: TextView
    private lateinit var postList: RecyclerView
    private var listPost: MutableList<PostList> = mutableListOf<PostList>()
    private var adapterrr: PostListFragment? = null
    private var selectedCategory: String? = null // 선택한 카테고리를 저장할 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentPostBinding.inflate(inflater, container, false)
        val rootView = _binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSpinner()

        super.onViewCreated(view, savedInstanceState)
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_post_list)
//
//        //txtData = findViewById(R.id.txtData)
////        postList = findViewById(R.id.post)
//
//        adapterrr = PostListFragment(
//            this,
//            listPost
//        )
//        postList.adapter = adapterrr
//
//        getUserList()
//    }

    private fun getUserList() {

        lifecycleScope.launch {
            try {
                val response = MyApi.retrofitService.viewCategoryPost(category = selectedCategory.toString())
                //Log.e("%%%%", response.toString())
                //txtData.text = response.toString()
                listPost.clear()
                response.data?.let {
                    listPost.addAll(it)
                }
                adapterrr?.notifyDataSetChanged()

            }catch (Ex: Exception){
                Log.e("@@@@Error", Ex.localizedMessage)
            }
        }
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
                    // Do nothing
                }
            }
    }
}