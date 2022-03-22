package com.example.okl_app_mvvm.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.FragmentAnimalsBinding
import com.example.okl_app_mvvm.ui.AnimalScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnimalsFragment : Fragment(R.layout.fragment_animals) {

    private var _binding: FragmentAnimalsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimalsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.zubr.setOnClickListener {
            val intent = Intent(activity, AnimalScreen::class.java)
            intent.putExtra("intent_animal", "zubr")
            startActivity(intent)
        }
        binding.konikPolski.setOnClickListener {
            val intent = Intent(activity, AnimalScreen::class.java)
            intent.putExtra("intent_animal", "konik")
            startActivity(intent)
        }
        binding.dzik.setOnClickListener {
            val intent = Intent(activity, AnimalScreen::class.java)
            intent.putExtra("intent_animal", "dzik")
            startActivity(intent)
        }
        binding.daniel.setOnClickListener {
            val intent = Intent(activity, AnimalScreen::class.java)
            intent.putExtra("intent_animal", "sarna")
            startActivity(intent)
        }
    }



}



