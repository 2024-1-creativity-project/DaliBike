package com.example.dali_bike

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.models.UserViewModel
import com.example.dali_bike.models.mainInfo
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment(), OnMapReadyCallback {
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val riderTxt: TextView = view.findViewById(R.id.rider_txt)

        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                // user 데이터가 null이 아닐 때, API 호출
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val response = apiService.userMainInfo(user.userId)

                        if (response.isSuccessful) {
                            val mainResList = response.body()
                            if (mainResList != null && mainResList.isNotEmpty()) {
                                val mainRes = mainResList[0]
                                withContext(Dispatchers.Main) {
                                    user.nickname = mainRes.nickname
                                    user.dailyTime = mainRes.dailyTime
                                    riderTxt.text = user.nickname
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("MainFragment", "Network request failed", e)
                    }
                }
            }
        })





        // val myPageBtn: Button = view.findViewById(R.id.myPageFragment)
        val myPageBtn: AppCompatImageView = view.findViewById(R.id.myPageBtn)
        val ridingTimerBtn: AppCompatImageButton = view.findViewById(R.id.ridingTimer_btn)
        val ridingCalBtn: AppCompatImageButton = view.findViewById(R.id.ridingCal_btn)
        val hotPostBtn: AppCompatImageButton = view.findViewById(R.id.hotpost_btn)
        locationSource = FusedLocationSource(requireActivity(), LOCATION_PERMISSION_REQUEST_CODE)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().replace(R.id.mapView, it).commit()
            }

        mapFragment.getMapAsync(this)

        val mapButton: ImageButton = view.findViewById(R.id.mapBtn)

        mapButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_naverMapFragment)
        }

        //val postBtn: ImageButton = view.findViewById()

        val mapBtn: AppCompatImageButton = view.findViewById(R.id.mapImg_btn)

        mapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_naverMapFragment)
        }
        myPageBtn.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_myPageFragment)
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

