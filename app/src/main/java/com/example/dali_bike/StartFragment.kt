package com.example.dali_bike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dali_bike.databinding.FragmentStartBinding
import androidx.navigation.fragment.findNavController

class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        val rootView = binding.root

        // 버튼 클릭 이벤트 설정
        binding.nextBtn.setOnClickListener {
            // 네비게이션 액션 실행
            findNavController().navigate(R.id.action_startFragment_to_start2Fragment)
        }

        return rootView
    }
}

