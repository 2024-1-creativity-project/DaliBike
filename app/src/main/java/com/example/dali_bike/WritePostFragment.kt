package com.example.dali_bike

import UserViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
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
        super.onViewCreated(view, savedInstanceState)

        val userId = userViewModel.user.value?.userId.toString()

        val editCategory: EditText = view.findViewById(R.id.categoryInput)
        val editTitle: EditText = view.findViewById(R.id.titleInput)
        val editContents: EditText = view.findViewById(R.id.contentsInput)
        //backBtn 만들기

        val finishBtn: ImageButton = view.findViewById(R.id.write_finishBtn)

        //작성완료 버튼 클릭 시
        finishBtn.setOnClickListener{
            //입력 값 가져옴
            val category = editCategory.text.toString()
            val title = editTitle.text.toString()
            val content = editContents.text.toString()

            //유효성 검사
            if(category.isEmpty()) {
                editCategory.error = "카테고리를 입력하세요"
                return@setOnClickListener
            }
            if(category!="자유게시판" && category!="만남의광장" && category!="공유게시판" && category!="hello") {
                editCategory.error = "잘못된 카테고리입니다."
                return@setOnClickListener
            }
            if(title.isEmpty()) {
                editTitle.error = "제목을 입력하세요"
                return@setOnClickListener
            }
            if(content.isEmpty()) {
                editContents.error = "내용을 입력하세요"
                return@setOnClickListener
            }
            val writePost = WritePost(userId = userId, categoryId = category, title = title, content = content)

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

}