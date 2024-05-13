package com.example.dali_bike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dali_bike.databinding.FragmentStart3Binding

class Start3Fragment : Fragment() {

    private lateinit var binding: FragmentStart3Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStart3Binding.inflate(inflater, container, false)
        val rootView = binding.root

        // 버튼 클릭 이벤트 설정
        binding.nextBtn.setOnClickListener {
            // 네비게이션 액션 실행
            findNavController().navigate(R.id.action_start3Fragment_to_start4Fragment)
        }

        return rootView
    }
}

