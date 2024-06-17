package com.example.dali_bike

import UserViewModel
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.model.Comment
import com.example.dali_bike.model.Item
import com.example.dali_bike.model.getComment
import com.example.dali_bike.model.getPostId
import com.example.dali_bike.model.like
import com.example.dali_bike.repository.Repository
import com.example.dali_bike.ui.MarkerWrapper
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostDetailFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var commentBox: LinearLayout
    private lateinit var like_num: TextView
    private lateinit var comment_num: TextView
    private var isChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_detail, container, false)

        val postId = arguments?.getString("postId").toString().toInt()

        val title_txt: TextView = view.findViewById(R.id.title_txt)
        val content_txt: TextView = view.findViewById(R.id.content_txt)
        val post_nick: TextView = view.findViewById(R.id.post_nick_txt)
        like_num = view.findViewById(R.id.like_num)
        comment_num = view.findViewById(R.id.comment_num)
        val comment: EditText = view.findViewById(R.id.InputComment)
        val commentBtn: ImageButton = view.findViewById(R.id.SendComment)
        val likeBtn: CheckBox = view.findViewById(R.id.CheckBox_Like)
        commentBox = view.findViewById(R.id.commentBox)
        val userId = userViewModel.user.value?.userId.toString()


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.postInfo(postId.toString())

                if (response.isSuccessful) {
                    val responList = response.body()

                    withContext(Dispatchers.Main) {
                        if (responList != null) {
                            val main = responList[0]

                            title_txt.text = main.Title
                            content_txt.text = main.Content
                            post_nick.text = main.NickName


                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            fetchLikeCommentCount(getPostId(postId))
            fetchGetComment(getPostId(postId))
            commentBtn.setOnClickListener {
                if(comment.text.toString() != "") {
                    fetchPostComment(Comment(postId, userId, comment.text.toString()))
                    comment.text = SpannableStringBuilder("")

                }
                fetchGetComment(getPostId(postId))
                fetchLikeCommentCount(getPostId(postId))
            }

            likeBtn.setOnClickListener {
                isChecked = !isChecked // 상태를 반전시킴
                if (isChecked) {
                    fetchLike(like(postId, 1))
                    fetchLikeCommentCount(getPostId(postId))
                } else {
                    fetchLike(like(postId, -1))
                    fetchLikeCommentCount(getPostId(postId))
                }
            }
        }

        return view
    }

    private fun fetchLike(like: like) {
        val repository = Repository()

        repository.postLike({ commnet ->
            commnet?.let {

            }

        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchPostComment: ${error.message}", Toast.LENGTH_LONG).show()
        }, like)
    }
    private fun fetchLikeCommentCount(postId: getPostId) {
        val repository = Repository()

        repository.postCount({ count ->
            count?.let {
                if (it[0].Like >= 1000) {
                    like_num.text = "+999"
                }
                else {
                    like_num.text = it[0].Like.toString()
                }
                if (it[0].CommentCount >= 1000) {
                    comment_num.text = "+999"
                }
                else {
                    comment_num.text = it[0].CommentCount.toString()
                }
            }

        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchPostComment: ${error.message}", Toast.LENGTH_LONG).show()
        }, postId)
    }

    private fun fetchPostComment(comment: Comment) {
        val repository = Repository()

        repository.postComment({ commnet ->
            commnet?.let {

            }

        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchPostComment: ${error.message}", Toast.LENGTH_LONG).show()
        }, comment)
    }

    private fun fetchGetComment(postId: getPostId) {
        val repository = Repository()
        val list = mutableListOf<getComment>() // 빈 리스트 생성

        repository.getComment({ commnet ->
            commnet?.let {
                list.addAll(it) // 받아온 정보를 리스트에 추가
                createTextView(list)
            }

        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchGetComment: ${error.message}", Toast.LENGTH_LONG).show()
        }, postId)
    }

    private fun createTextView(items: List<getComment>) {
        commentBox.removeAllViews()
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.laundrygothicbold)

        for ((index, item) in items.withIndex()) {
            val comment = item.Comment
            val nickname = item.Nickname

            // TextView 생성
            val textView = TextView(requireContext()).apply {
                text = "$nickname : $comment"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    // 상단 마진을 10dp에서 5dp로 변경
                    topMargin = 5.dpToPx(requireContext())  // dp를 픽셀로 변환
                }
                // 폰트 적용
                typeface?.let {
                    this.typeface = it
                }
            }
            commentBox.addView(textView)
        }
    }
    fun Int.dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapBtn: AppCompatImageButton = view.findViewById(R.id.mapBtn)
        val mainBtn: AppCompatImageButton = view.findViewById(R.id.homeBtn)
        val postBtn: AppCompatImageButton = view.findViewById(R.id.postBtn)
        val myPageBtn: AppCompatImageButton = view.findViewById(R.id.myPageBtn)
        val backBtn: AppCompatImageButton =view.findViewById(R.id.back_btn)

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

        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_postDetailFragment_to_postFragment2)
        }
    }
}