package com.example.dali_bike

import UserViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.api.apiService
import com.example.dali_bike.models.InquiryMonthlyInfo
import com.example.dali_bike.models.InquiryMyRank
import com.example.dali_bike.models.InquiryRank
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class CalendarFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val homeBtn: AppCompatImageButton = view.findViewById(R.id.homeBtn)
        val postBtn: AppCompatImageButton = view.findViewById(R.id.postBtn)
        val mapBtn: AppCompatImageButton = view.findViewById(R.id.mapBtn)
        val myPageBtn: AppCompatImageButton = view.findViewById(R.id.myPageBtn)

        val first_nick: TextView = view.findViewById(R.id.first_nick)
        val second_nick: TextView = view.findViewById(R.id.second_nick)
        val third_nick: TextView = view.findViewById(R.id.third_nick)

        val first_time: TextView = view.findViewById(R.id.first_time)
        val second_time: TextView = view.findViewById(R.id.second_time)
        val third_time: TextView = view.findViewById(R.id.third_time)

        val my_rank: TextView = view.findViewById(R.id.my_rank)
        val my_nick: TextView = view.findViewById(R.id.my_nickName)
        val my_time: TextView = view.findViewById(R.id.my_time)

        val dayRecord: TextView = view.findViewById(R.id.dayRecord)

        val month_txt: TextView = view.findViewById(R.id.month_txt)

        val id = userViewModel.user.value?.userId.toString()

        val calendar: CalendarView = view.findViewById(R.id.calendar)
        val date = Calendar.getInstance()
        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH) + 1

        // 선택된 날짜에 대한 데이터를 가져오는 함수
        fun fetchDataForDate(year: Int, month: Int, day: Int) {
            // 선택된 날짜에 대한 일일 정보를 가져오고 표시하는 코루틴
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val monthInfo = InquiryMonthlyInfo(id, year, month)
                    val response = apiService.myMonthly(monthInfo)

                    if (response.isSuccessful) {
                        val monthlyInfo = response.body()

                        withContext(Dispatchers.Main) {
                            if (monthlyInfo != null) {
                                val dayStr = String.format("%02d", day)
                                for (monthInfo in monthlyInfo) {
                                    val datePart = monthInfo.date.substring(8, 10)

                                    if (datePart == dayStr) {
                                        val dayTime = monthInfo.dailyTime
                                        val dayTimeH = dayTime / 360000
                                        val dayTimeM = (dayTime / 6000) % 60
                                        val dayTimeS = dayTime / 100 % 60
                                        dayRecord.text = "${dayTimeH}h ${dayTimeM}m ${dayTimeS}s"
                                        break
                                    } else {
                                        dayRecord.text = "0h 0m 0s"
                                    }
                                }
                            }
                        }
                    } else {
                        dayRecord.text = "0h 0m 0s"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // 선택된 월에 대한 순위를 가져오고 표시하는 코루틴
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val monthRank = InquiryRank(year, month)
                    val response = apiService.rank(monthRank)

                    if (response.isSuccessful) {
                        val rankInfo = response.body()

                        withContext(Dispatchers.Main) {
                            if (rankInfo != null) {
                                var time: Int
                                var timeH: Int
                                var timeM: Int
                                var timeS: Int
                                var formatTime = ""

                                if (rankInfo[0].Nickname == "0") {
                                    first_nick.text = null
                                    second_nick.text = null
                                    third_nick.text = null

                                    first_time.text = null
                                    second_time.text = null
                                    third_time.text = null
                                } else if (rankInfo.size == 1) {
                                    first_nick.text = rankInfo[0].Nickname
                                    second_nick.text = null
                                    third_nick.text = null

                                    time = rankInfo[0].totalTime
                                    timeH = time / 360000
                                    timeM = (time / 6000) % 60
                                    timeS = time / 100 % 60

                                    formatTime = "${timeH}h ${timeM}m ${timeS}s"
                                    first_time.text = formatTime

                                    second_time.text = null
                                    third_time.text = null
                                } else if (rankInfo.size == 2) {
                                    first_nick.text = rankInfo[0].Nickname
                                    second_nick.text = rankInfo[1].Nickname
                                    third_nick.text = null

                                    time = rankInfo[0].totalTime
                                    timeH = time / 360000
                                    timeM = (time / 6000) % 60
                                    timeS = time / 100 % 60

                                    formatTime = "${timeH}h ${timeM}m ${timeS}s"
                                    first_time.text = formatTime

                                    time = rankInfo[1].totalTime
                                    timeH = time / 360000
                                    timeM = (time / 6000) % 60
                                    timeS = time / 100 % 60

                                    formatTime = "${timeH}h ${timeM}m ${timeS}s"
                                    second_time.text = formatTime
                                    third_time.text = null
                                } else {
                                    first_nick.text = rankInfo[0].Nickname
                                    second_nick.text = rankInfo[1].Nickname
                                    third_nick.text = rankInfo[2].Nickname

                                    time = rankInfo[0].totalTime
                                    timeH = time / 360000
                                    timeM = (time / 6000) % 60
                                    timeS = time / 100 % 60

                                    formatTime = "${timeH}h ${timeM}m ${timeS}s"
                                    first_time.text = formatTime

                                    time = rankInfo[1].totalTime
                                    timeH = time / 360000
                                    timeM = (time / 6000) % 60
                                    timeS = time / 100 % 60

                                    formatTime = "${timeH}h ${timeM}m ${timeS}s"
                                    second_time.text = formatTime

                                    time = rankInfo[2].totalTime
                                    timeH = time / 360000
                                    timeM = (time / 6000) % 60
                                    timeS = time / 100 % 60

                                    formatTime = "${timeH}h ${timeM}m ${timeS}s"
                                    third_time.text = formatTime
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // 선택된 월에 대한 사용자의 순위를 가져오고 표시하는 코루틴
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val monthlyInfo = InquiryMyRank(id, year, month)
                    val response = apiService.myRank(monthlyInfo)

                    if (response.isSuccessful) {
                        val myMonthInfo = response.body()

                        withContext(Dispatchers.Main) {
                            if (myMonthInfo != null) {
                                if (myMonthInfo.rank == 0) {
                                    my_rank.text = "-"
                                }
                                else {
                                    val rank = myMonthInfo.rank.toString()
                                    my_rank.text = "${rank}등"
                                }
                                my_nick.text = myMonthInfo.Nickname

                                val time = myMonthInfo.totalTime
                                val timeH = time / 360000
                                val timeM = (time / 6000) % 60
                                val timeS = time / 100 % 60

                                val formatTime = "${timeH}h ${timeM}m ${timeS}s"
                                my_time.text = formatTime

                                month_txt.text = "${month}월 랭킹"
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // 달력에 날짜 변경 리스너를 설정
        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            fetchDataForDate(year, month + 1, dayOfMonth)
        }

        // 현재 날짜에 대한 초기 데이터 가져오기
        val day = date.get(Calendar.DAY_OF_MONTH)
        fetchDataForDate(year, month, day)

        homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_calendarFragment_to_mainFragment)
        }
        postBtn.setOnClickListener {
            findNavController().navigate(R.id.action_calendarFragment_to_postFragment)
        }
        mapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_calendarFragment_to_naverMapFragment)
        }
        myPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_calendarFragment_to_myPageFragment)
        }
    }
}
