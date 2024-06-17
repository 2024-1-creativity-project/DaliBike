package com.example.dali_bike

import UserViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.databinding.FragmentWritepostBinding
import com.example.dali_bike.models.WritePost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WritePostFragment : Fragment() {
    private lateinit var _binding: FragmentWritepostBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private var selectedCategory: String? = null // 선택한 카테고리를 저장할 변수


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentWritepostBinding.inflate(inflater, container, false)
        val rootView = _binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSpinner()

        super.onViewCreated(view, savedInstanceState)

        val userId = userViewModel.user.value?.userId.toString()

        val editCategory: EditText = view.findViewById(R.id.categoryInput)
        val editTitle: EditText = view.findViewById(R.id.titleInput)
        val editContents: EditText = view.findViewById(R.id.contentsInput)
        //backBtn 만들기

        val mapBtn: AppCompatImageButton = view.findViewById(R.id.mapBtn)
        val postBtn: AppCompatImageButton = view.findViewById(R.id.postBtn)
        val myPageBtn: AppCompatImageButton = view.findViewById(R.id.myPageBtn)
        val homeBtn: AppCompatImageButton = view.findViewById(R.id.homeBtn)
        val backBtn: AppCompatImageButton = view.findViewById(R.id.back_btn)

        val finishBtn: ImageButton = view.findViewById(R.id.write_finishBtn)

        mapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_writePost_to_naverMapFragment)
        }

        postBtn.setOnClickListener {
            findNavController().navigate(R.id.action_writePost_to_postFragment)
        }

        myPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_writePost_to_myPageFragment)
        }

        homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_writePost_to_mainFragment)
        }

        homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_writePost_to_postFragment)
        }

        //작성완료 버튼 클릭 시
        finishBtn.setOnClickListener{
            //입력 값 가져옴
            val category = editCategory.text.toString()
            val title = editTitle.text.toString()
            val content = editContents.text.toString()

            //유효성 검사
            if(selectedCategory.toString().isEmpty()) {
                editCategory.error = "카테고리를 입력하세요"
                return@setOnClickListener
            }
//            if(category!="자유게시판" && category!="만남의광장" && category!="공유게시판" && category!="hello") {
//                editCategory.error = "잘못된 카테고리입니다."
//                return@setOnClickListener
//            }
            if(title.isEmpty()) {
                editTitle.error = "제목을 입력하세요"
                return@setOnClickListener
            }
            if(content.isEmpty()) {
                editContents.error = "내용을 입력하세요"
                return@setOnClickListener
            }
            val writePost = WritePost(userId = userId, categoryId = selectedCategory.toString(), title = title, content = content)

//            Log.d("category",writePost.Category)
//            Log.d("title",writePost.Title)
//            Log.d("content",writePost.Content)
//            Log.d("userId",writePost.USERId)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.writePost(writePost)

                    if (response.isSuccessful) {
                        val wriRes = response.body()
                        withContext(Dispatchers.Main){
                            if (wriRes?.result == "true") {
                                    Toast.makeText(context, "게시글 등록이 완료되었습니다", Toast.LENGTH_LONG).show()
                                    findNavController().navigate(R.id.action_writePost_to_postFragment)
                                } else {
                                    Toast.makeText(context, "게시글 등록을 할 수 없습니다", Toast.LENGTH_LONG).show()
                                    editCategory.error = "카테고리를 확인해주세요"
                                    editTitle.error = "제목을 확인해주세요"
                                    editContents.error = "내용을 확인해주세요"
                                }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "서버 응답이 올바르지 않습니다", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "게시글 등록 실패: 네트워크 오류", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }


    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.post_category,
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
