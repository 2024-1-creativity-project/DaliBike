package com.example.dali_bike

import UserViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.models.ID
import com.example.dali_bike.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class MyPageFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)

        val nickTxt: TextView = view.findViewById(R.id.nickTxt)
        val idTxt: TextView = view.findViewById(R.id.idTxt)
        val regDateTxt: TextView = view.findViewById(R.id.regDateTxt)
        val pointTxt: TextView = view.findViewById(R.id.pointTxt)

        val postName1: TextView = view.findViewById(R.id.myPostName1)
        val postName2: TextView = view.findViewById(R.id.myPostName2)
        val post1: TextView = view.findViewById(R.id.myPost1)
        val post2: TextView = view.findViewById(R.id.myPost2)
        val like1: TextView = view.findViewById(R.id.myLike1)
        val like2: TextView = view.findViewById(R.id.myLike2)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val id = ID(userViewModel.user.value?.userId.toString())
                val response = apiService.myInfo(id)

                if (response.isSuccessful) {
                    val myResList = response.body()
                    if (myResList != null && myResList.isNotEmpty()) {
                        val myRes = myResList[0]
                        withContext(Dispatchers.Main) {
                            userViewModel.setUserMyPage(myRes.Name, myRes.Points, myRes.subDate)

                            nickTxt.text = myRes.Nickname
                            idTxt.text = myRes.USERId

                            val subDateStr = myRes.subDate
                            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                            val date = inputFormat.parse(subDateStr)

                            val formattedDate = date?.let { outputFormat.format(it) }

                            regDateTxt.text = formattedDate

                            pointTxt.text = myRes.Points.toString()

                        }
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val id = userViewModel.user.value?.userId.toString()
                val response = apiService.myPost(id)
                val titleMax = 10
                val contentMax= 15

                if  (response.isSuccessful) {
                    val postList = response.body()
                    if(postList != null) {
                        withContext(Dispatchers.Main) {
                            postName1.text = postList[0].Title.truncateWithEllipsis(titleMax)
                            postName2.text = postList[1].Title.truncateWithEllipsis(titleMax)
                            post1.text = postList[0].Content.truncateWithEllipsis(contentMax)
                            post2.text = postList[1].Content.truncateWithEllipsis(contentMax)
                            if (postList[0].Like >= 1000) {
                                like1.text = "999+"
                            }
                            else {
                                like1.text = postList[0].Like.toString()
                            }

                            if (postList[1].Like >= 1000) {
                                like2.text = "999+"
                            }
                            else {
                                like2.text = postList[1].Like.toString()
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
        val homeBtn: AppCompatImageButton = view.findViewById(R.id.homeBtn)
        val postBtn: AppCompatImageButton = view.findViewById(R.id.postBtn)
        val myPageBtn: AppCompatImageButton = view.findViewById(R.id.myPageBtn)
        val logoutBtn: Button = view.findViewById(R.id.logoutBtn)
        val moreBtn: Button = view.findViewById(R.id.moreBtn)

        mapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_naverMapFragment)
        }

        homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_mainFragment)
        }

        postBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_postFragment)
        }

        myPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_self)
        }

        logoutBtn.setOnClickListener {
            val user = User(
                userId = "",
                password = "",
                phoneNumber = "",
                name = "",
                nickname = "",
                points = 0,
                subDate = "",
                dailyTime = 0,
                totalTime = 0
            )
            userViewModel.setUser(user)
            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_myPageFragment_to_loginFragment)
        }

        moreBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_myPostFragment)
        }
    }
}