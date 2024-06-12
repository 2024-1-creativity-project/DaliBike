package com.example.dali_bike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatImageButton
import androidx.navigation.fragment.findNavController

import android.widget.Spinner
import com.example.dali_bike.databinding.FragmentPostBinding

class PostFragment : Fragment() {
    private lateinit var binding: FragmentPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        val rootView = binding.root
        return rootView
    }
//        val spinner: Spinner = view.findViewById(R.id.categorySpinner)

//        val items = arrayOf("자유게시판", "만남의광장", "공유게시판")
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
////        spinner.adapter = adapter
//
//        return view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val writePostBtn: AppCompatImageButton = view.findViewById(R.id.writePostBtn)
        writePostBtn.setOnClickListener {
            findNavController().navigate(R.id.action_postFragment_to_writePost)
        }



    }
}

