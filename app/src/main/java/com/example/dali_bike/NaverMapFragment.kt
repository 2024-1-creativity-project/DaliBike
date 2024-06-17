package com.example.dali_bike.ui

import UserViewModel
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dali_bike.R
import com.example.dali_bike.model.Item
import com.example.dali_bike.model.Record
import com.example.dali_bike.model.RecordUSERId
import com.example.dali_bike.model.Report
import com.example.dali_bike.model.ReportCancel
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.concurrent.timer
import android.graphics.BitmapFactory
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.model.RecordViewModel

data class MarkerWrapper(val marker: Marker, val itemNum: Int)

class NaverMapFragment : Fragment(), OnMapReadyCallback {
    private val recordViewModel: RecordViewModel by activityViewModels()

    lateinit var scroll: ScrollView

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

    // 상세 정보
    lateinit var detailbox: ConstraintLayout

    lateinit var report_detail:LinearLayout
    lateinit var report_type_detail0: ImageView
    lateinit var report_type_detail1: ImageView
    lateinit var report_img_detail: ImageView
    lateinit var report_cancel_button: ImageButton

    lateinit var lodging_detail:LinearLayout
    lateinit var lodgingName_detail: TextView
    lateinit var lodgingPhoneNumber_detail: TextView
    lateinit var lodgingLocationAddress_detail: TextView
    lateinit var lodgingLocationPostcode_detail: TextView
    lateinit var lodgingRoadAddress_detail: TextView
    lateinit var lodgingRoadPostcode_detail: TextView

    lateinit var store_detail:LinearLayout
    lateinit var storeName_detail: TextView
    lateinit var storePhoneNumber_detail: TextView
    lateinit var storeLocationAddress_detail: TextView
    lateinit var storeRoadAddress_detail: TextView
    lateinit var storeDayOff_detail: TextView
    lateinit var storeStartTime_detail: TextView
    lateinit var storeEndTime_detail: TextView

    lateinit var rental_detail:LinearLayout
    lateinit var rentalName_detail: TextView
    lateinit var rentalType_detail: TextView
    lateinit var rentalPhoneNumber_detail: TextView
    lateinit var rentalLocationAddress_detail: TextView
    lateinit var rentalRoadAddress_detail: TextView
    lateinit var rentalDayOff_detail: TextView
    lateinit var rentalStartTime_detail: TextView
    lateinit var rentalEndTime_detail: TextView
    lateinit var rentalIsFare_detail: TextView
    lateinit var rentalFare_detail: TextView

    private var userId : String = ""
    private var isStartRecord : Boolean = false

    // 기록
    lateinit var driveStart_button: ImageButton
    lateinit var driveEnd_button: ImageButton
    lateinit var drive_constraintLayout: ConstraintLayout

    lateinit var tv_hour: TextView
    lateinit var tv_minute: TextView
    lateinit var tv_second: TextView

    var isRunning = false
    var timer : Timer? = null // ❶ timer 변수 추가
    var time=0 // ❷ time 변수 추가

    // 제보
    lateinit var danger_report_button: ImageButton
    lateinit var danger_report_detail: LinearLayout
    lateinit var danger_report_pothole_button: ImageButton
    lateinit var danger_report_construction_button: ImageButton
    lateinit var danger_report_pothole_select_button: ImageButton
    lateinit var danger_report_construction_select_button: ImageButton
    lateinit var danger_report_camera_button: ImageButton
    lateinit var danger_report_complete: ImageButton


    lateinit var danger_cancel_report_detail: LinearLayout
    lateinit var danger_cancel_report_camera_button: ImageButton
    lateinit var danger_cancel_report_complete: ImageButton
    private var bitmap: Bitmap? = null
    var lat: Double = 0.0
    var lon: Double = 0.0

    val REQUEST_IMAGE_CAPTURE = 1
    var isSelectDangerPothole = false
    var isSelectDangerConstruction = false

    private var isPhotoTaken = false // 사용자가 사진을 찍었는지 여부를 추적하는 변수 추가
    private var dangerReportImage: Bitmap? = null

    var isFabOpen = false
    var isChecked1 = true
    var isChecked2 = true
    var isChecked3 = true
    var isChecked4 = true
    var isChecked5 = true
    var isChecked6 = true

    private val userViewModel: UserViewModel by activityViewModels()

    // 마커 리스트를 MarkerWrapper로 변경
    private val markerList = mutableListOf<MarkerWrapper>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //메뉴BAR
        val myPageBtn: AppCompatImageButton = view.findViewById(R.id.writePost_Btn)
        val homeBtn: AppCompatImageButton = view.findViewById(R.id.homeBtn)
        val postBtn: AppCompatImageButton = view.findViewById(R.id.postBtn)
        val mapBtn: AppCompatImageButton = view.findViewById(R.id.mapBtn)

        myPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_naverMapFragment_to_myPageFragment)
        }

        homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_naverMapFragment_to_mainFragment)
        }

        postBtn.setOnClickListener {
            findNavController().navigate(R.id.action_naverMapFragment_to_postFragment)
        }

        mapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_naverMapFragment_self)
        }


        // FloatingActionButton과 CheckBox들을 초기화
        fab = view.findViewById(R.id.fab)
        checkBox1 = view.findViewById(R.id.checkBox_danger)
        checkBox2 = view.findViewById(R.id.checkBox_store)
        checkBox3 = view.findViewById(R.id.checkBox_airinjector)
        checkBox4 = view.findViewById(R.id.checkBox_lodging)
        checkBox5 = view.findViewById(R.id.checkBox_rental)
        checkBox6 = view.findViewById(R.id.checkBox_storagefacility)
        checkboxtool = view.findViewById(R.id.checkboxtool)

        detailbox = view.findViewById(R.id.detailbox)

        report_detail = view.findViewById(R.id.report_detail)
        report_type_detail0 = view.findViewById(R.id.report_type_detail0)
        report_type_detail1 = view.findViewById(R.id.report_type_detail1)
        report_img_detail = view.findViewById(R.id.report_img_detail)
        report_cancel_button = view.findViewById(R.id.report_cancel_button)


        lodging_detail = view.findViewById(R.id.lodging_detail)
        lodgingName_detail = view.findViewById(R.id.text_lodgingName_detail)
        lodgingPhoneNumber_detail = view.findViewById(R.id.text_lodgingPhoneNumber_detail)
        lodgingLocationAddress_detail = view.findViewById(R.id.text_lodgingLocationAddress_detail)
        lodgingLocationPostcode_detail = view.findViewById(R.id.text_lodgingLocationPostcode_detail)
        lodgingRoadAddress_detail = view.findViewById(R.id.text_lodgingRoadAddress_detail)
        lodgingRoadPostcode_detail = view.findViewById(R.id.text_lodgingRoadPostcode_detail)

        store_detail = view.findViewById(R.id.store_detail)
        storeName_detail = view.findViewById(R.id.text_storeName_detail)
        storePhoneNumber_detail = view.findViewById(R.id.text_storePhoneNumber_detail)
        storeLocationAddress_detail = view.findViewById(R.id.text_storeLocationAddress_detail)
        storeRoadAddress_detail = view.findViewById(R.id.text_storeRoadAddress_detail)
        storeDayOff_detail = view.findViewById(R.id.text_storeDayOff_detail)
        storeStartTime_detail = view.findViewById(R.id.text_storeStartTime)
        storeEndTime_detail = view.findViewById(R.id.text_storeEndTime_detail)

        rental_detail = view.findViewById(R.id.rental_detail)
        rentalName_detail = view.findViewById(R.id.text_rentalName_detail)
        rentalType_detail = view.findViewById(R.id.text_rentalType_detail)
        rentalPhoneNumber_detail = view.findViewById(R.id.text_rentalPhoneNumber_detail)
        rentalLocationAddress_detail = view.findViewById(R.id.text_rentalLocationAddress_detail)
        rentalRoadAddress_detail = view.findViewById(R.id.text_rentalRoadAddress_detail)
        rentalDayOff_detail = view.findViewById(R.id.text_rentalDayOff_detail)
        rentalStartTime_detail = view.findViewById(R.id.text_rentalStartTime_detail)
        rentalEndTime_detail = view.findViewById(R.id.text_rentalEndTime_detail)
        rentalIsFare_detail = view.findViewById(R.id.text_rentalIsFare_detail)
        rentalFare_detail = view.findViewById(R.id.text_rentalFare_detail)

        driveStart_button = view.findViewById(R.id.imageButton_record_drivestart)
        driveEnd_button = view.findViewById(R.id.imageButton_record_driveend)
        drive_constraintLayout = view.findViewById(R.id.ConstraintLayout_record_drive)

        //chronometer = view.findViewById(R.id.chronometer)
        tv_hour = view.findViewById(R.id.tv_hour)
        tv_minute = view.findViewById(R.id.tv_minute)
        tv_second = view.findViewById(R.id.tv_second)

        danger_report_button = view.findViewById(R.id.imageButton_danger_report)
        danger_report_detail = view.findViewById(R.id.danger_report_detail)
        danger_report_pothole_button = view.findViewById(R.id.imageButton_danger_report_pothole)
        danger_report_construction_button = view.findViewById(R.id.imageButton_danger_report_construction)
        danger_report_pothole_select_button = view.findViewById(R.id.imageButton_danger_report_pothole_select)
        danger_report_construction_select_button = view.findViewById(R.id.imageButton_danger_report_construction_select)
        danger_report_camera_button = view.findViewById(R.id.imageButton_danger_report_camera)
        danger_report_complete = view.findViewById(R.id.imageButton_danger_report_complete)

        scroll = view.findViewById(R.id.scroll)
        danger_cancel_report_detail = view.findViewById(R.id.danger_cancel_report_detail)
        danger_cancel_report_camera_button = view.findViewById(R.id.imageButton_cancel_danger_report_camera)
        danger_cancel_report_complete = view.findViewById(R.id.imageButton_cancel_danger_report_complete)

        userId = userViewModel.user.value?.userId.toString()
        isStartRecord = recordViewModel.record.value?.startRecording ?: false

        // FloatingActionButton 클릭 리스너 설정
        fab.setOnClickListener {
            if (isFabOpen) {
                closeFabMenu() // CheckBox를 숨김
            } else {
                showFabMenu() // CheckBox를 표시
            }
            isFabOpen = !isFabOpen // 상태를 반전시킴
        }

        driveStart_button.setOnClickListener {
            driveStart_button.visibility = View.GONE
            drive_constraintLayout.visibility = View.VISIBLE
            fetchTodayRecord(RecordUSERId(id = userId))
            start()
        }
        driveEnd_button.setOnClickListener {
            pause()
            fetchRecord(Record(id=userId,dailyTime=time))
            driveStart_button.visibility = View.VISIBLE
            drive_constraintLayout.visibility = View.GONE
        }

        if(isStartRecord){
            driveStart_button.performClick()
            recordViewModel.setIsRecordClicked(false)
            isStartRecord = false
        }

        danger_report_button.setOnClickListener {
            if (isFabOpen) {
                closeDangerReport() // 숨김
                closeItemDetail()
            } else {ReportDetail()
                showDangerReport() // 표시
            }
            isFabOpen = !isFabOpen // 상태를 반전시킴
        }

    }
    private fun showDangerReport() {
        detailbox.visibility = View.VISIBLE
        scroll.visibility = View.VISIBLE
        danger_report_detail.visibility = View.VISIBLE
        danger_report_construction_button.isEnabled = true
        danger_report_pothole_button.isEnabled = true
        danger_report_camera_button.isEnabled = true
        danger_report_complete.isEnabled = true

        // 페이드 인 애니메이션 설정
        val fadeIn = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 300 // 애니메이션 지속 시간
            fillAfter = true // 애니메이션 후 상태 유지
        }

        // CheckBox에 애니메이션 적용
        detailbox.startAnimation(fadeIn)
        scroll.startAnimation(fadeIn)
        danger_report_detail.startAnimation(fadeIn)
    }

    private fun showDangerCancelReport() {
        scroll.visibility = View.VISIBLE
        danger_cancel_report_detail.visibility = View.VISIBLE
        danger_cancel_report_camera_button.isEnabled = true
        danger_cancel_report_complete.isEnabled = true
        // 페이드 인 애니메이션 설정
        val fadeIn = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 300 // 애니메이션 지속 시간
            fillAfter = true // 애니메이션 후 상태 유지
        }

        // CheckBox에 애니메이션 적용
        scroll.startAnimation(fadeIn)
        danger_cancel_report_detail.startAnimation(fadeIn)

    }

    private fun closeDangerReport() {
        // 페이드 아웃 애니메이션 설정
        val fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 300 // 애니메이션 지속 시간
            fillAfter = true // 애니메이션 후 상태 유지
        }

        // CheckBox에 애니메이션 적용
        detailbox.startAnimation(fadeOut)
        scroll.startAnimation(fadeOut)
        danger_report_detail.startAnimation(fadeOut)


        // 애니메이션 리스너 설정
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            // 애니메이션 종료 후 CheckBox를 숨김
            override fun onAnimationEnd(animation: Animation?) {
                detailbox.visibility = View.GONE
                scroll.visibility = View.GONE

                danger_report_construction_button.visibility = View.VISIBLE
                danger_report_pothole_button.visibility = View.VISIBLE
                danger_report_construction_select_button.visibility = View.GONE
                danger_report_pothole_select_button.visibility = View.GONE
                danger_report_camera_button.isEnabled = false
                danger_report_complete.isEnabled = false

                isPhotoTaken = false
                isSelectDangerPothole = false
                isSelectDangerConstruction = false

                danger_report_camera_button.setImageDrawable(resources.getDrawable(R.drawable.icon_xml_danger_report_camera_button))

            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun closeDangerCancelReport() {
        // 페이드 아웃 애니메이션 설정
        val fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 300 // 애니메이션 지속 시간
            fillAfter = true // 애니메이션 후 상태 유지
        }

        // CheckBox에 애니메이션 적용
        detailbox.startAnimation(fadeOut)
        scroll.startAnimation(fadeOut)
        danger_cancel_report_detail.startAnimation(fadeOut)

        // 애니메이션 리스너 설정
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            // 애니메이션 종료 후 CheckBox를 숨김
            override fun onAnimationEnd(animation: Animation?) {
                detailbox.visibility = View.GONE
                scroll.visibility = View.GONE
                danger_cancel_report_detail.visibility = View.GONE
                danger_cancel_report_camera_button.isEnabled = false
                danger_cancel_report_complete.isEnabled = false

                isPhotoTaken = false

                danger_cancel_report_camera_button.setImageDrawable(resources.getDrawable(R.drawable.icon_xml_danger_report_camera_button))

            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
    private fun ReportDetail() {

        danger_report_pothole_button.setOnClickListener {
            isSelectDangerPothole = true
            danger_report_pothole_button.visibility = View.GONE
            danger_report_pothole_select_button.visibility = View.VISIBLE
            // 비활성화
            danger_report_construction_button.isEnabled = false
        }
        danger_report_pothole_select_button.setOnClickListener {
            isSelectDangerPothole = false
            danger_report_pothole_button.visibility = View.VISIBLE
            danger_report_pothole_select_button.visibility = View.GONE
            // 비활성화
            danger_report_construction_button.isEnabled = true
        }

        danger_report_construction_button.setOnClickListener {
            isSelectDangerConstruction = true
            danger_report_construction_button.visibility = View.GONE
            danger_report_construction_select_button.visibility = View.VISIBLE
            // 비활성화
            danger_report_pothole_button.isEnabled = false
        }
        danger_report_construction_select_button.setOnClickListener {
            isSelectDangerConstruction = false
            danger_report_construction_button.visibility = View.VISIBLE
            danger_report_construction_select_button.visibility = View.GONE
            // 비활성화
            danger_report_pothole_button.isEnabled = true

        }

        checkPermission()
        danger_report_camera_button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (hasCameraPermission()) {
                    startCamera()
                } else {
                    requestCameraPermission()
                }
            }
        }

        danger_report_complete.setOnClickListener {
            if (isPhotoTaken && (isSelectDangerPothole || isSelectDangerConstruction)) {
                // 사진을 찍고 포트홀 또는 공사정보 중 하나를 선택했을 때만 작동
                val type = if (isSelectDangerPothole) 0 else 1
                val report = Report(userId = userId, type = type, latitude = lat, longitude = lon)

                // Bitmap을 파일로 변환
                val imageFile = bitmapToFile(dangerReportImage!!)

                // 보고서와 파일을 서버로 전송
                fetchReport(imageFile, report)
                Toast.makeText(requireContext(), "위험 전송 완료.", Toast.LENGTH_LONG).show()

                closeDangerReport()

            } else {
                // 사진을 찍거나 포트홀 또는 공사정보 중 하나를 선택하지 않았을 때 경고 또는 작업 구현
                Toast.makeText(requireContext(), "위험 정보 종류와 사진을 찍으세요.", Toast.LENGTH_LONG).show()
            }
        }

    }

    // 카메라 권한을 확인하는 함수
    private fun checkPermission() {
        // 카메라 권한이 없고 안드로이드 버전이 M 이상인 경우
        if (!hasCameraPermission() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 카메라 권한 요청
            requestCameraPermission()
        }
    }

    // 카메라 권한이 있는지 확인하는 함수
    private fun hasCameraPermission(): Boolean {
        // 카메라 권한이 허용되어 있는지를 반환
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    // 카메라 권한을 요청하는 함수
    private fun requestCameraPermission() {
        // 카메라 권한 요청
        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_IMAGE_CAPTURE)
        }
    }

    // 카메라 앱을 실행하는 함수
    private fun startCamera() {
        // 카메라 앱 실행을 위한 인텐트 생성
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // 카메라 앱이 있는지 확인하고 실행
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    // 카메라로부터 이미지를 받아오는 결과를 처리하는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 요청 코드가 카메라로부터 이미지를 받아오는 요청이고 결과가 성공적인 경우
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            // 받아온 이미지를 ImageView에 설정
            dangerReportImage  = data?.extras?.get("data") as Bitmap
            danger_report_camera_button.setImageBitmap(dangerReportImage)
            danger_cancel_report_camera_button.setImageBitmap(dangerReportImage)

            isPhotoTaken = true
        }
    }

    // 1. Bitmap을 파일로 변환
    private fun bitmapToFile(bitmap: Bitmap): File {
        // 새 파일 생성
        val file = File(requireContext().cacheDir, "temp_image.jpg")
        file.createNewFile()

        // 파일에 비트맵 저장
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return file
    }


    private fun fetchReport(imageFile: File, report: Report) {
        val repository = Repository()

        // 파일을 RequestBody로 변환
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())

        // MultipartBody.Part 생성
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        repository.postReport({ reportResult ->
            reportResult?.let {
                // 성공 처리
            }
        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchReport: ${error.message}", Toast.LENGTH_LONG).show()
        }, report, imagePart)
    }


    private fun fetchTodayRecord(recordUSERId: RecordUSERId) {
        val repository = Repository()
        repository.postViewToday({ record ->
            record?.let {
                time = it[0].dailyTime
            }
        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchRecord: ${error.message}", Toast.LENGTH_LONG).show()
        }, recordUSERId)
    }


    private fun fetchRecord(record: Record) {
        val repository = Repository()
        repository.postRecord({ recordSave ->
            recordSave?.let {
                if(it.result=="true"){
                    Toast.makeText(requireContext(), "기록 저장 성공", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(requireContext(), "기록 저장 실패", Toast.LENGTH_LONG).show()
                }
            }
        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchRecord: ${error.message}", Toast.LENGTH_LONG).show()
        }, record)
    }

    private fun start() {
        // ❻ 스톱워치 측정을 시작하는 로직
        isRunning = true // ❷ 실행 상태 변경
        // ❸ 스톱워치를 시작하는 로직
        timer = timer(period = 10) {
            time++ // ❹ 10밀리초 단위 타이머
            // ❺ 시간 계산
            var second = time / 100 % 60
            var minute = (time / 6000) % 60
            var hour = time / 360000

            // UI 업데이트를 메인 스레드에서 수행
            activity?.runOnUiThread {
                // 시
                if (hour < 10) tv_hour.text = "0${hour}"
                else tv_hour.text = "${hour}"
                // 분
                if (minute < 10) tv_minute.text = ":0${minute}"
                else tv_minute.text = ":${minute}"
                // 초
                if (second < 10) tv_second.text = ":0${second}"
                else tv_second.text = ":${second}"
            }
        }
    }


    private fun pause() {
        // ❶ 텍스트 속성 변경
        isRunning = false // ❷ 멈춤 상태로 전환
        timer?.cancel() // ❸ 타이머 멈추기
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

        // 위치 변경이 인식되는 경우
        naverMap.addOnLocationChangeListener { location ->
            lat = location.latitude
            lon = location.longitude
        }

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
            val fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
                duration = 300 // 애니메이션 지속 시간
                fillAfter = true // 애니메이션 후 상태 유지
            }
            detailbox.startAnimation(fadeOut)
            // 애니메이션 리스너 설정
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                // 애니메이션 종료 후 CheckBox를 숨김
                override fun onAnimationEnd(animation: Animation?) {
                    detailbox.visibility = View.GONE
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
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
            val id: Int
            if (itemNum == 1){
                id = item.ReportId
            }
            else{
                id = item.Id
            }

            val marker = Marker()
            marker.position = LatLng(latitude, longitude)
            marker.map = naverMap
            getItemService(itemNum, marker)

            marker.setOnClickListener {
                getItemDetailService(itemNum, id)
                isFabOpen = !isFabOpen // 상태를 반전시킴
                true
            }
            markerList.add(MarkerWrapper(marker, itemNum))  // 마커를 MarkerWrapper로 추가
        }
    }
    private fun showItemDetail(detailItem: LinearLayout) {
        detailbox.visibility = View.VISIBLE
        detailItem.visibility = View.VISIBLE
        if(detailItem == rental_detail){
            scroll.visibility = View.VISIBLE
        }

        if(detailItem == report_detail){
            report_cancel_button.visibility = View.VISIBLE
            report_cancel_button.isEnabled = true
        }

        val constraintSet = ConstraintSet()

        // 페이드 인 애니메이션 설정
        val fadeIn = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 300 // 애니메이션 지속 시간
            fillAfter = true // 애니메이션 후 상태 유지
        }

        // lodging_detail 애니메이션 적용
        detailItem.startAnimation(fadeIn)
        detailbox.startAnimation(fadeIn)
        if(detailItem == rental_detail){
            scroll.startAnimation(fadeIn)
        }
        if(detailItem == report_detail){
            report_cancel_button.startAnimation(fadeIn)
        }
    }

    private fun closeItemDetail() {
        // 페이드 아웃 애니메이션 설정
        val fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 1 // 애니메이션 지속 시간
            fillAfter = true // 애니메이션 후 상태 유지
        }

        // lodging_detail 애니메이션 적용
        report_detail.startAnimation(fadeOut)
        report_cancel_button.startAnimation(fadeOut)
        lodging_detail.startAnimation(fadeOut)
        store_detail.startAnimation(fadeOut)
        rental_detail.startAnimation(fadeOut)
        scroll.startAnimation(fadeOut)
        danger_report_detail.startAnimation(fadeOut)
        danger_cancel_report_detail.startAnimation(fadeOut)
        detailbox.startAnimation(fadeOut)

        // 애니메이션 리스너 설정
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            // 애니메이션 종료 후 CheckBox를 숨김
            override fun onAnimationEnd(animation: Animation?) {
                report_detail.visibility = View.GONE
                report_cancel_button.visibility = View.GONE
                lodging_detail.visibility = View.GONE
                store_detail.visibility = View.GONE
                rental_detail.visibility = View.GONE
                scroll.visibility = View.GONE
                danger_report_detail.visibility = View.GONE
                danger_cancel_report_detail.visibility = View.GONE
                detailbox.visibility = View.GONE
                danger_cancel_report_camera_button.isEnabled = false
                danger_cancel_report_complete.isEnabled = false
                danger_report_camera_button.isEnabled = false
                danger_report_complete.isEnabled = false
                report_cancel_button.isEnabled = false
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun getItemDetailService(itemNum: Int, indexNum: Int){
        when (itemNum) {
            1 -> {
                if (isFabOpen) {
                    closeItemDetail()  // CheckBox를 숨김
                    closeDangerCancelReport()
                } else {
                    DangerReportCancel(indexNum)
                    fetchReportDetail(indexNum)
                    fetchReportImg(indexNum)
                    showItemDetail(report_detail) // CheckBox를 표시
                    report_cancel_button.setOnClickListener {
                        // 페이드 아웃 애니메이션 설정
                        val fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
                            duration = 1 // 애니메이션 지속 시간
                            fillAfter = true // 애니메이션 후 상태 유지
                        }

                        // lodging_detail 애니메이션 적용
                        report_detail.startAnimation(fadeOut)
                        report_cancel_button.startAnimation(fadeOut)

                        // 애니메이션 리스너 설정
                        fadeOut.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {}

                            // 애니메이션 종료 후 CheckBox를 숨김
                            override fun onAnimationEnd(animation: Animation?) {
                                report_detail.visibility = View.GONE
                                report_cancel_button.visibility = View.GONE
                                report_cancel_button.isEnabled = false
                            }

                            override fun onAnimationRepeat(animation: Animation?) {}
                        })
                        showDangerCancelReport()
                    }
                }
            }

            2 -> {
                if (isFabOpen) {fetchStoreDetail(indexNum)
                    closeItemDetail()  // CheckBox를 숨김
                }
                else {
                    showItemDetail(store_detail ) // CheckBox를 표시
                }
            }
            4 -> {
                if (isFabOpen) {fetchLodgingDetail(indexNum)
                    closeItemDetail()  // CheckBox를 숨김
                }
                else {
                    showItemDetail(lodging_detail ) // CheckBox를 표시
                }
            }
            5 -> {
                if (isFabOpen) {fetchRentalDetail(indexNum)
                    closeItemDetail()  // CheckBox를 숨김
                }
                else {
                    showItemDetail(rental_detail ) // CheckBox를 표시
                }
            }
        }
    }


    private fun DangerReportCancel (indexNum: Int){
        danger_cancel_report_camera_button.isEnabled = true
        danger_cancel_report_complete.isEnabled = true
        checkPermission()
        danger_cancel_report_camera_button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (hasCameraPermission()) {
                    startCamera()
                } else {
                    requestCameraPermission()
                }
            }
        }


        danger_cancel_report_complete.setOnClickListener {
            if (isPhotoTaken) {
                // 사진을 찍고 포트홀 또는 공사정보 중 하나를 선택했을 때만 작동
                val reportCancel =
                    ReportCancel(userId = userId, reportId = indexNum)

                // Bitmap을 파일로 변환
                val imageFile = bitmapToFile(dangerReportImage!!)

                // 보고서와 파일을 서버로 전송
                fetchReportCancel(imageFile, reportCancel)
                Toast.makeText(requireContext(), "위험 취소 전송 완료.", Toast.LENGTH_LONG)
                    .show()

                closeItemDetail()
                closeDangerCancelReport()

            } else {
                // 사진을 찍거나 포트홀 또는 공사정보 중 하나를 선택하지 않았을 때 경고 또는 작업 구현
                Toast.makeText(
                    requireContext(),
                    "위험 취소 정보 사진을 찍으세요.",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

    }


    private fun fetchReportCancel(imageFile: File, reportCancel: ReportCancel) {
        val repository = Repository()

        // 파일을 RequestBody로 변환
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())

        // MultipartBody.Part 생성
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        repository.postReportCancel({ reportResult ->
            reportResult?.let {
                // 성공 처리
            }
        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchReportCancel: ${error.message}", Toast.LENGTH_LONG).show()
        }, reportCancel, imagePart)
    }

    private fun fetchReportDetail(indexNum: Int) {
        val repository = Repository()

        repository.getReportDetail({ report ->
            report?.let {

                if(it.type == 0){
                    report_type_detail0.visibility = View.VISIBLE
                    report_type_detail1.visibility = View.GONE
                }
                else if(it.type == 1){
                    report_type_detail0.visibility = View.GONE
                    report_type_detail1.visibility = View.VISIBLE
                }
            }

        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchReportDetail: ${error.message}", Toast.LENGTH_LONG).show()
        }, indexNum)
    }

    private fun fetchReportImg(indexNum : Int) {
        val repository = Repository()

        repository.getReportImg({ img ->
            img?.let {
                val inputStream = it.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                report_img_detail.setImageBitmap(bitmap)
            }

        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchReportImg: ${error.message}", Toast.LENGTH_LONG).show()
        }, indexNum)
    }

    private fun fetchLodgingDetail(indexNum: Int) {
        val repository = Repository()

        repository.getLodgingDetailItem({ lodging ->
            lodging?.let {
                // lodgingDetailItem이 성공적으로 로드된 후에 값을 사용합니다.
                lodgingName_detail.text = it[0].BusinessName
                lodgingPhoneNumber_detail.text = it[0].LocationPhoneNumber
                lodgingLocationAddress_detail.text = it[0].LocationAddress
                lodgingLocationPostcode_detail.text = it[0].LocationPostcode
                lodgingRoadAddress_detail.text = it[0].RoadAddress
                lodgingRoadPostcode_detail.text = it[0].RoadPostcode
            }

        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchLodgingDetail: ${error.message}", Toast.LENGTH_LONG).show()
        }, indexNum)
    }

    private fun fetchStoreDetail(indexNum: Int) {
        val repository = Repository()

        repository.getStoreDetailItem({ store ->
            store?.let {
                storeName_detail.text = it[0].StoreName
                storePhoneNumber_detail.text = it[0].StorePhone
                storeLocationAddress_detail.text = it[0].LocalAddress
                storeRoadAddress_detail.text = it[0].RoadAddress
                storeDayOff_detail.text = it[0].DayOff
                storeStartTime_detail.text = it[0].StartTime
                storeEndTime_detail.text = it[0].EndTime
            }

        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchStoreDetail: ${error.message}", Toast.LENGTH_LONG).show()
        }, indexNum)
    }

    private fun fetchRentalDetail(indexNum: Int) {
        val repository = Repository()

        repository.getRentalDetailItem({ rental ->
            rental?.let {
                rentalName_detail.text = it[0].RSName
                if(it[0].ManRS == 1 && it[0].UnmanRS ==1){
                    rentalType_detail.text = "유인,무인 대여소"
                }
                else if(it[0].ManRS == 1){
                    rentalType_detail.text = "유인 대여소"
                }
                else if(it[0].UnmanRS == 1){
                    rentalType_detail.text = "무인 대여소"
                }
                rentalPhoneNumber_detail.text = it[0].ManagePhone
                rentalLocationAddress_detail.text = it[0].LocalAddress
                rentalRoadAddress_detail.text = it[0].RoadAddress
                rentalDayOff_detail.text = it[0].DayOff
                rentalStartTime_detail.text = it[0].StartTime
                rentalEndTime_detail.text = it[0].EndTime
                if(it[0].IsFare == 1){
                    rentalIsFare_detail.text = "유료"
                }
                else{
                    rentalIsFare_detail.text = "무료"
                }
                rentalFare_detail.text = it[0].Fare
            }

        }, { error ->
            // 오류 처리
            Toast.makeText(requireContext(), "fetchRentalDetail: ${error.message}", Toast.LENGTH_LONG).show()
        }, indexNum)
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
