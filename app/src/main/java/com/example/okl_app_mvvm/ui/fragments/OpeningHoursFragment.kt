package com.example.okl_app_mvvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.FragmentAboutBinding
import com.example.okl_app_mvvm.databinding.FragmentOpeningHoursBinding

class OpeningHoursFragment : Fragment(R.layout.fragment_opening_hours) {
    private var _binding: FragmentOpeningHoursBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpeningHoursBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}