package com.example.okl_app_mvvm.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.okl_app_mvvm.ui.GoogleMapsActivity
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.FragmentHomeBinding
import com.example.okl_app_mvvm.ui.MainActivity
import com.example.okl_app_mvvm.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding
    get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.about.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
        }
        binding.map.setOnClickListener{
            val intent = Intent(requireContext(), GoogleMapsActivity::class.java)
            startActivity(intent)
        }
        binding.animals.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_animalsFragment)
        }
        binding.openingHours.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_openingHoursFragment)
        }
        binding.settings.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        binding.englishlang.setOnClickListener(){
            changeLangEN()
        }
        binding.polishlang.setOnClickListener(){
            changeLangPL()
        }


    }

    private fun changeLangEN() {
        context?.let { setAppLocale(it, "en") }
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun changeLangPL(){
        context?.let { setAppLocale(it, "pl") }
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

}