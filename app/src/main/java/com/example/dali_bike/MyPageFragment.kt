package com.example.dali_bike

import UserViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.models.ID
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapBtn: AppCompatImageButton = view.findViewById(R.id.mapBtn)
        val homeBtn: AppCompatImageButton = view.findViewById(R.id.homeBtn)
        val postBtn: AppCompatImageButton = view.findViewById(R.id.postBtn)
        val myPageBtn: AppCompatImageButton = view.findViewById(R.id.myPageBtn)
        val logoutBtn: Button = view.findViewById(R.id.logoutBtn)

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
            findNavController().navigate(R.id.action_myPageFragment_to_loginFragment)
        }
    }
}