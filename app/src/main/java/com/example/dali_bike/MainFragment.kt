package com.example.dali_bike

import UserViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.model.RecordViewModel
import com.example.dali_bike.model.mainHotPost
import com.example.dali_bike.model.rentalDetailItem
import com.example.dali_bike.models.ID
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainFragment : Fragment(), OnMapReadyCallback  {
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val userViewModel: UserViewModel by activityViewModels()
    private val viewHotPost: UserViewModel by activityViewModels()
    private val recordViewModel: RecordViewModel by activityViewModels()

    //    fun viewHotPost(searchView: String?, completion: (String) -> Unit) {
//        val call: Call<JsonElement>? = apiService?.hotPost( )
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val riderTxt: TextView = view.findViewById(R.id.rider_txt)
        val dailyTimeTxt: TextView = view.findViewById(R.id.ridingCalanderTime)
        val totalTimeTxt: TextView = view.findViewById(R.id.totalTime_txt)
        val today: TextView = view.findViewById(R.id.ridingCalanderToday)

        val hotPost1: TextView = view.findViewById(R.id.hotPost1)
        val hotPost2: TextView = view.findViewById(R.id.hotPost2)
        val hotPost3: TextView = view.findViewById(R.id.hotPost3)
        val hotPost4: TextView = view.findViewById(R.id.hotPost4)
        val hotPost5: TextView = view.findViewById(R.id.hotPost5)
        val hotPost6: TextView = view.findViewById(R.id.hotPost6)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val id = ID(userViewModel.user.value?.userId.toString())

                val response = apiService.userMainInfo(id)

                if (response.isSuccessful) {
                    val mainResList = response.body()

                    withContext(Dispatchers.Main) {
                        if (mainResList != null) {
                            val mainRes = mainResList[0]
                            userViewModel.setUserMain(mainRes.Nickname, mainRes.dailyTime, mainRes.totalTime)

                            //닉네임 보여주기
                            riderTxt.text = mainRes.Nickname

                            //dailyTime 보여주기
                            var dailyTime = mainRes.dailyTime
                            val dailyH = dailyTime / 360000
                            val dailyM = (dailyTime / 6000) % 60
                            val dailyS = dailyTime / 100 % 60

                            val timeFormat = "${dailyH}h ${dailyM}m ${dailyS}s"
                            dailyTimeTxt.text = timeFormat

                            //totalTime 보여주기
                            var totalTime = mainRes.totalTime
                            val totalH = totalTime / 360000
                            val totalM = (totalTime / 6000) % 60
                            val totalS = totalTime / 100 % 60

                            val formatTime = "${totalH}h ${totalM}m ${totalS}s"
                            totalTimeTxt.text = formatTime

                            val calendar = Calendar.getInstance()
                            val dateFormat = SimpleDateFormat("M월 d일", Locale.getDefault())
                            val todayDate = dateFormat.format(calendar.time)

                            today.text = todayDate
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = apiService.getHotPost()
            val maxlength = 10


            if (response.isSuccessful) {
                    val hotResList = response.body()
                    Log.d("가져오는 값", hotResList.toString())
                    withContext(Dispatchers.Main) {
                        if (hotResList != null && hotResList.isNotEmpty()) {
                            hotPost1.text = hotResList[0].Title.truncateWithEllipsis(maxlength)
                            hotPost2.text = hotResList[1].Title.truncateWithEllipsis(maxlength)
                            hotPost3.text = hotResList[2].Title.truncateWithEllipsis(maxlength)
                            hotPost4.text = hotResList[3].Title.truncateWithEllipsis(maxlength)
                            hotPost5.text = hotResList[4].Title.truncateWithEllipsis(maxlength)
                            hotPost6.text = hotResList[5].Title.truncateWithEllipsis(maxlength)
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

        val ridingTimerBtn: AppCompatImageButton = view.findViewById(R.id.ridingTimer_btn)
        val ridingCalBtn: AppCompatImageButton = view.findViewById(R.id.ridingCal_btn)
        val hotPostBtn: AppCompatImageButton = view.findViewById(R.id.hotpost_btn)
        locationSource = FusedLocationSource(requireActivity(), LOCATION_PERMISSION_REQUEST_CODE)

        fun postViewHot(searchView: SearchView, completion: (String) -> Unit) {

        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().replace(R.id.mapView, it).commit()
            }

        mapFragment.getMapAsync(this)

        val mapButton: AppCompatImageButton = view.findViewById(R.id.mapBtn)

        mapButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_naverMapFragment)
        }

        val mainBtn: AppCompatImageButton = view.findViewById(R.id.homeBtn)

        mainBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_self)
        }

        val mapBtn: AppCompatImageButton = view.findViewById(R.id.mapImg_btn)

        mapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_naverMapFragment)
        }

        val myPageBtn: AppCompatImageButton = view.findViewById(R.id.myPageBtn)
        myPageBtn.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_myPageFragment)
        }

        val postBtn: AppCompatImageButton = view.findViewById(R.id.postBtn)
        postBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_postFragment)
        }

        ridingCalBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_calendarFragment)
        }

        ridingTimerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_naverMapFragment)
            recordViewModel.setIsRecordClicked(true)
        }
        hotPostBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_postFragment)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}

fun String.truncateWithEllipsis(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.substring(0, maxLength) + "..."
    } else {
        this
    }
}
