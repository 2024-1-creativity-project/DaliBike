package com.example.dali_bike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.navigation.fragment.findNavController
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

class MainFragment : Fragment(), OnMapReadyCallback {
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

