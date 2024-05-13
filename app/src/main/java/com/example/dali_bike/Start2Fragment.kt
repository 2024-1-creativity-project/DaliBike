package com.example.dali_bike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.databinding.FragmentStart2Binding

class Start2Fragment : Fragment() {

    private lateinit var binding: FragmentStart2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStart2Binding.inflate(inflater, container, false)
        val rootView = binding.root

        // 버튼 클릭 이벤트 설정
        binding.nextBtn.setOnClickListener {
            // 네비게이션 액션 실행
            findNavController().navigate(R.id.action_start2Fragment_to_start3Fragment)
        }

        return rootView
    }
}

