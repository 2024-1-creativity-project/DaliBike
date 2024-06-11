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

class MainFragment : Fragment(), OnMapReadyCallback  {
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val userViewModel: UserViewModel by activityViewModels()
    private val viewHotPost: UserViewModel by activityViewModels()

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

        var hotPost1: TextView = view.findViewById(R.id.hotPost1)
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
                            val dailyH = dailyTime / 3600
                            dailyTime -= dailyH * 3600
                            val dailyM = dailyTime / 60
                            dailyTime -= dailyM * 60
                            val dailyS = dailyTime

                            val timeFormat = "${dailyH}h ${dailyM}m ${dailyS}s"
                            dailyTimeTxt.text = timeFormat

                            //totalTime 보여주기
                            var totalTime = mainRes.totalTime
                            val totalH = totalTime / 3600
                            totalTime -= totalH * 3600
                            val totalM = totalTime / 60
                            totalTime -= totalM * 60
                            val totalS = totalTime

                            val formatTime = "${totalH}h ${totalM}m ${totalS}s"
                            totalTimeTxt.text = formatTime
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

                if (response.isSuccessful) {
                    val hotResList = response.body()
                    Log.d("가져오는 값", hotResList.toString())
                    withContext(Dispatchers.Main) {
                        if (hotResList != null && hotResList.isNotEmpty()) {
                            hotPost1.text = hotResList[0].Title
                            hotPost2.text = hotResList[1].Title
                            hotPost3.text = hotResList[2].Title
                            hotPost4.text = hotResList[3].Title
                            hotPost5.text = hotResList[4].Title
                            hotPost6.text = hotResList[5].Title
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

        //val postBtn: ImageButton = view.findViewById()

        val mapBtn: AppCompatImageButton = view.findViewById(R.id.mapImg_btn)

        mapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_naverMapFragment)
        }

        val myPageBtn: AppCompatImageView = view.findViewById(R.id.imgView)
        //이거 ImageView니까 Button으로 고치기
        myPageBtn.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_myPageFragment)
        }

        val postBtn: AppCompatImageButton = view.findViewById(R.id.postBtn)
        postBtn.setOnClickListener {
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

