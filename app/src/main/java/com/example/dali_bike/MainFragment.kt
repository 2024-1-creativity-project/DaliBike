package com.example.dali_bike

import UserViewModel
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.models.ID
import com.example.dali_bike.models.User
import com.example.dali_bike.models.mainInfo
import com.google.gson.JsonElement
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.CoroutineScope
import org.w3c.dom.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.Body

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

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val id = ID(userViewModel.user.value?.userId.toString())
                val response = apiService.userMainInfo(id)

                if (response.isSuccessful) {
                    val mainRes = response.body()

                    withContext(Dispatchers.Main) {
                        if (mainRes != null) {
                            userViewModel.setUserMain(mainRes.nickname, mainRes.dailyTime)
                            riderTxt.text = mainRes.nickname
//                            dailyTimeTxt.text = mainRes.dailyTime.toString()
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

        // val myPageBtn: Button = view.findViewById(R.id.myPageFragment)
        val myPageBtn: AppCompatImageView = view.findViewById(R.id.myPageBtn)
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

