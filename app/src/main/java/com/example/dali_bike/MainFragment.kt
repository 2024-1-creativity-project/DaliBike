package com.example.dali_bike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // val myPageBtn: Button = view.findViewById(R.id.myPageFragment)
        val mapBtn: AppCompatImageButton = view.findViewById(R.id.map_btn)
        val myPageBtn: AppCompatImageButton = view.findViewById(R.id.myPage_btn)
        val ridingTimerBtn: AppCompatImageButton = view.findViewById(R.id.ridingTimer_btn)
        val ridingCalBtn: AppCompatImageButton = view.findViewById(R.id.ridingCal_btn)
        val hotPostBtn: AppCompatImageButton = view.findViewById(R.id.hotpost_btn)

        mapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_naverMapFragment)
        }
        myPageBtn.setOnClickListener({
            findNavController().navigate(R.id.action_mainFragment_to_myPageFragment)
        })


    }
}

