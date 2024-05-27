package com.example.dali_bike.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dali_bike.R
import com.example.dali_bike.model.Item
import com.example.dali_bike.model.lodgingDetailItem
import com.example.dali_bike.repository.Repository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

data class MarkerWrapper(val marker: Marker, val itemNum: Int)

class NaverMapFragment : Fragment(), OnMapReadyCallback {

    // 지도 객체 변수
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    // 체크박스 및 FloatingActionButton
    lateinit var fab: FloatingActionButton
    lateinit var checkBox1: CheckBox
    lateinit var checkBox2: CheckBox
    lateinit var checkBox3: CheckBox
    lateinit var checkBox4: CheckBox
    lateinit var checkBox5: CheckBox
    lateinit var checkBox6: CheckBox
    lateinit var checkboxtool: ImageView

    lateinit var businessName_detail: TextView
    lateinit var locationAddress_detail: TextView
    lateinit var locationPhoneNumber_detail: TextView
    lateinit var locationPostcode_detail: TextView
    lateinit var roadAddress_detail: TextView
    lateinit var roadPostcode_detail: TextView

//    lateinit var dayOff_detail: TextView
//    lateinit var endTime_detail: TextView
//    lateinit var fare_detail: TextView
//    lateinit var isFare_detail: TextView
//    lateinit var localAddress_detail: TextView
//    lateinit var manRS_detail: TextView
//    lateinit var managePhone_detail: TextView
//    lateinit var rSName_detail: TextView
//    lateinit var roadAddress_detail: TextView
//    lateinit var startTime_detail: TextView
//    lateinit var unmanRS_detail: TextView

    lateinit var lodging_detail:LinearLayout
    var isFabOpen = false
    var isChecked1 = true
    var isChecked2 = true
    var isChecked3 = true
    var isChecked4 = true
    var isChecked5 = true
    var isChecked6 = true

    private fun fetchRentalDetail(indexNum: Int) {
        val repository = Repository()

        repository.getLodgingDetailItem(indexNum, { lodging ->
            lodging?.let {
                // lodgingDetailItem이 성공적으로 로드된 후에 값을 사용합니다.
                businessName_detail.text = it[0].BusinessName
                locationPhoneNumber_detail.text = it[0].LocationPhoneNumber
                locationAddress_detail.text = it[0].LocationAddress
                locationPostcode_detail.text = it[0].LocationPostcode
                roadAddress_detail.text = it[0].RoadAddress
                roadPostcode_detail.text = it[0].RoadPostcode
            }

        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchLodgingDetail: ${error.message}", Toast.LENGTH_LONG).show()
        })
    }

    // 마커 리스트를 MarkerWrapper로 변경
    private val markerList = mutableListOf<MarkerWrapper>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FloatingActionButton과 CheckBox들을 초기화
        fab = view.findViewById(R.id.fab)
        checkBox1 = view.findViewById(R.id.checkBox_danger)
        checkBox2 = view.findViewById(R.id.checkBox_store)
        checkBox3 = view.findViewById(R.id.checkBox_airinjector)
        checkBox4 = view.findViewById(R.id.checkBox_lodging)
        checkBox5 = view.findViewById(R.id.checkBox_rental)
        checkBox6 = view.findViewById(R.id.checkBox_storagefacility)
        checkboxtool = view.findViewById(R.id.checkboxtool)

        lodging_detail = view.findViewById(R.id.lodging_detail)
        businessName_detail = view.findViewById(R.id.text_businessName_detail)
        locationPhoneNumber_detail = view.findViewById(R.id.text_locationPhoneNumber_detail)
        locationAddress_detail = view.findViewById(R.id.text_locationAddress_detail)
        locationPostcode_detail = view.findViewById(R.id.text_locationPostcode_detail)
        roadAddress_detail = view.findViewById(R.id.text_roadAddress_detail)
        roadPostcode_detail = view.findViewById(R.id.text_roadPostcode_detail)

        // FloatingActionButton 클릭 리스너 설정
        fab.setOnClickListener {
            if (isFabOpen) {
                closeFabMenu() // CheckBox를 숨김
            } else {
                showFabMenu() // CheckBox를 표시
            }
            isFabOpen = !isFabOpen // 상태를 반전시킴
        }


    }

    private fun showFabMenu() {
        checkBox1.visibility = View.VISIBLE
        checkBox2.visibility = View.VISIBLE
        checkBox3.visibility = View.VISIBLE
        checkBox4.visibility = View.VISIBLE
        checkBox5.visibility = View.VISIBLE
        checkBox6.visibility = View.VISIBLE
        checkboxtool.visibility = View.VISIBLE

        // 페이드 인 애니메이션 설정
        val fadeIn = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 300 // 애니메이션 지속 시간
            fillAfter = true // 애니메이션 후 상태 유지
        }

        // CheckBox에 애니메이션 적용
        checkBox1.startAnimation(fadeIn)
        checkBox2.startAnimation(fadeIn)
        checkBox3.startAnimation(fadeIn)
        checkBox4.startAnimation(fadeIn)
        checkBox5.startAnimation(fadeIn)
        checkBox6.startAnimation(fadeIn)
        checkboxtool.startAnimation(fadeIn)
    }

    private fun closeFabMenu() {
        // 페이드 아웃 애니메이션 설정
        val fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 300 // 애니메이션 지속 시간
            fillAfter = true // 애니메이션 후 상태 유지
        }

        // CheckBox에 애니메이션 적용
        checkBox1.startAnimation(fadeOut)
        checkBox2.startAnimation(fadeOut)
        checkBox3.startAnimation(fadeOut)
        checkBox4.startAnimation(fadeOut)
        checkBox5.startAnimation(fadeOut)
        checkBox6.startAnimation(fadeOut)
        checkboxtool.startAnimation(fadeOut)

        // 애니메이션 리스너 설정
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            // 애니메이션 종료 후 CheckBox를 숨김
            override fun onAnimationEnd(animation: Animation?) {
                checkBox1.visibility = View.GONE
                checkBox2.visibility = View.GONE
                checkBox3.visibility = View.GONE
                checkBox4.visibility = View.GONE
                checkBox5.visibility = View.GONE
                checkBox6.visibility = View.GONE
                checkboxtool.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_naver_map, container, false) as ViewGroup

        mapView = rootView.findViewById(R.id.navermap)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // 현재 위치
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        // checkBox 초기화
        checkBox1 = rootView.findViewById(R.id.checkBox_danger)
        checkBox2 = rootView.findViewById(R.id.checkBox_store)
        checkBox3 = rootView.findViewById(R.id.checkBox_airinjector)
        checkBox4 = rootView.findViewById(R.id.checkBox_lodging)
        checkBox5 = rootView.findViewById(R.id.checkBox_rental)
        checkBox6 = rootView.findViewById(R.id.checkBox_storagefacility)


        return rootView
    }

    // 현재 위치 권한 부여
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(naverMap: NaverMap) {
        // 건물 표시
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true)

        // 현재 위치
        naverMap.locationSource = locationSource
        // 현재 위치 나타내는 마커 설정
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 지도 레이어 띄우기
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, false)

        // naverMap 객체 초기화
        this.naverMap = naverMap

        // checkBox1 상태 변경 감지를 위한 리스너 설정
        checkBox1.setOnClickListener {
            checkBoxClick(naverMap, isChecked1, 1)
            isChecked1 = !isChecked1
        }
        // checkBox2 상태 변경 감지를 위한 리스너 설정
        checkBox2.setOnClickListener {
            checkBoxClick(naverMap, isChecked2, 2)
            isChecked2 = !isChecked2
        }
        // checkBox3 상태 변경 감지를 위한 리스너 설정
        checkBox3.setOnClickListener {
            checkBoxClick(naverMap, isChecked3, 3)
            isChecked3 = !isChecked3
        }
        // checkBox4 상태 변경 감지를 위한 리스너 설정
        checkBox4.setOnClickListener {
            checkBoxClick(naverMap, isChecked4, 4)
            isChecked4 = !isChecked4
        }
        // checkBox5 상태 변경 감지를 위한 리스너 설정
        checkBox5.setOnClickListener {
            checkBoxClick(naverMap, isChecked5, 5)
            isChecked5 = !isChecked5
        }
        // checkBox6 상태 변경 감지를 위한 리스너 설정
        checkBox6.setOnClickListener {
            checkBoxClick(naverMap, isChecked6, 6)
            isChecked6 = !isChecked6
        }
    }


    private fun checkBoxClick(naverMap: NaverMap, isChecked: Boolean, itemNum: Int){
        // 상태에 따라 작업 수행
        if (isChecked) {
            fetchItemSaveList(naverMap,itemNum) // 체크되면 데이터를 불러옴
        } else {
            clearMarkers(itemNum) // 체크 해제되면 마커를 제거함
        }
    }

    // Repository에 있는 item response를 list로 변환 후 마커 찍는 메소드로
    private fun fetchItemSaveList(naverMap: NaverMap, itemNum: Int) {
        val repository = Repository()
        val list = mutableListOf<Item>() // 빈 리스트 생성
        repository.getItem({ items ->
            items?.let {
                list.addAll(it) // 받아온 정보를 리스트에 추가
                createMarkers(list, naverMap, itemNum)
            }
        }, { error ->
            Toast.makeText(requireContext(), "${error.message}", Toast.LENGTH_LONG).show()
        }, itemNum)
    }


    // 위도 경도 마커 찍기
    private fun createMarkers(items: List<Item>, naverMap: NaverMap, itemNum: Int) {

        for ((index, item) in items.withIndex()) {
            val latitude = item.Latitude
            val longitude = item.Longitude

            val marker = Marker()
            marker.position = LatLng(latitude, longitude)
            marker.map = naverMap
            getItemService(itemNum, marker)

            marker.setOnClickListener {
                Toast.makeText(requireContext(), "${itemNum} 마커 ${index + 1} 클릭", Toast.LENGTH_SHORT).show()
                getItemDetailService(itemNum, index + 1)
                isFabOpen = !isFabOpen // 상태를 반전시킴
                true
            }
            markerList.add(MarkerWrapper(marker, itemNum))  // 마커를 MarkerWrapper로 추가
        }
    }
    private fun showItemDetail(detailItem: LinearLayout) {
        detailItem.visibility = View.VISIBLE

        // 페이드 인 애니메이션 설정
        val fadeIn = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 300 // 애니메이션 지속 시간
            fillAfter = true // 애니메이션 후 상태 유지
        }

        // lodging_detail 애니메이션 적용
        detailItem.startAnimation(fadeIn)
    }

    private fun closeItemDetail(detailItem: LinearLayout) {
        // 페이드 아웃 애니메이션 설정
        val fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 300 // 애니메이션 지속 시간
            fillAfter = true // 애니메이션 후 상태 유지
        }

        // lodging_detail 애니메이션 적용
        detailItem.startAnimation(fadeOut)


        // 애니메이션 리스너 설정
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            // 애니메이션 종료 후 CheckBox를 숨김
            override fun onAnimationEnd(animation: Animation?) {
                detailItem.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun getItemDetailService(itemNum: Int, indexNum: Int){
        when (itemNum) {
            2 -> {
                if (isFabOpen) {fetchLodgingDetail(indexNum)
                    closeItemDetail(lodging_detail)  // CheckBox를 숨김
                }
                else {
                    showItemDetail(lodging_detail ) // CheckBox를 표시
                }
            }
            4 -> {
                if (isFabOpen) {fetchLodgingDetail(indexNum)
                    closeItemDetail(lodging_detail)  // CheckBox를 숨김
                }
                else {
                    showItemDetail(lodging_detail ) // CheckBox를 표시
                }
            }
            5 -> {
                if (isFabOpen) {fetchLodgingDetail(indexNum)
                    closeItemDetail(lodging_detail)  // CheckBox를 숨김
                }
                else {
                    showItemDetail(lodging_detail ) // CheckBox를 표시
                }
            }
        }
    }

    private fun fetchLodgingDetail(indexNum: Int) {
        val repository = Repository()

        repository.getLodgingDetailItem(indexNum, { lodging ->
            lodging?.let {
                // lodgingDetailItem이 성공적으로 로드된 후에 값을 사용합니다.
                businessName_detail.text = it[0].BusinessName
                locationPhoneNumber_detail.text = it[0].LocationPhoneNumber
                locationAddress_detail.text = it[0].LocationAddress
                locationPostcode_detail.text = it[0].LocationPostcode
                roadAddress_detail.text = it[0].RoadAddress
                roadPostcode_detail.text = it[0].RoadPostcode
            }

        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchLodgingDetail: ${error.message}", Toast.LENGTH_LONG).show()
        })
    }

    private fun getItemService(itemNum : Int, marker: Marker) {
        when(itemNum){
            1 -> marker.icon = OverlayImage.fromResource(R.drawable.icon_xml_marker_danger)
            2 -> marker.icon = OverlayImage.fromResource(R.drawable.icon_xml_marker_store)
            3 -> marker.icon = OverlayImage.fromResource(R.drawable.icon_xml_marker_airinjector)
            4 -> marker.icon = OverlayImage.fromResource(R.drawable.icon_xml_marker_lodging)
             5 -> marker.icon = OverlayImage.fromResource(R.drawable.icon_xml_marker_retal)
            6 -> marker.icon = OverlayImage.fromResource(R.drawable.icon_xml_marker_storagefacility)
        }
    }

    private fun clearMarkers(itemNum: Int) {
        val iterator = markerList.iterator()
        while (iterator.hasNext()) {
            val markerWrapper = iterator.next()
            if (markerWrapper.itemNum == itemNum) {
                markerWrapper.marker.map = null  // 마커를 지도에서 제거
                iterator.remove()  // 리스트에서 제거
            }
        }
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
