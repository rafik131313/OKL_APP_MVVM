package com.example.okl_app_mvvm.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.FragmentAboutBinding
import com.example.okl_app_mvvm.databinding.FragmentInformationBinding
import com.example.okl_app_mvvm.databinding.FragmentVisitingParkBinding
import com.example.okl_app_mvvm.ui.viewmodels.MainViewModel
import javax.inject.Inject

class InformationFragment : Fragment(R.layout.fragment_information) {

    private var _binding: FragmentInformationBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformationBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var blind = false

        binding.TVAboutMain.setOnClickListener{
            if (blind == false){
                binding.TVAboutMain.textSize = 30F
                blind = true
            }
            else
            {
                binding.TVAboutMain.textSize = 20F
                blind = false
            }
        }
    }
}