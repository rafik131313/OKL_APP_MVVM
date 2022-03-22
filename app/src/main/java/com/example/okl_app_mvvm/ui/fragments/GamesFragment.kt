package com.example.okl_app_mvvm.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.okl_app_mvvm.ui.QuizActivity
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.FragmentGamesBinding
import com.example.okl_app_mvvm.other.Constants
import com.example.okl_app_mvvm.ui.PictureChoose
import com.example.okl_app_mvvm.ui.ScoreBoard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GamesFragment : Fragment(R.layout.fragment_games) {

    private var _binding: FragmentGamesBinding? = null
    private val binding
    get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGamesBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pointsOverall = sharedPreferences.getInt(Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS, 0)
        binding.score.text = pointsOverall.toString()

        binding.photoRecognize.setOnClickListener {
            val intent = Intent(context, PictureChoose::class.java)
            startActivity(intent)
        }
        binding.visitingPark.setOnClickListener {
            findNavController().navigate(R.id.action_gamesFragment_to_visitingParkFragment)
        }
        binding.onPicture.setOnClickListener {
            findNavController().navigate(R.id.action_gamesFragment_to_signingFlowersFragment)
        }
        binding.quiz.setOnClickListener {
            val intent = Intent(context, QuizActivity::class.java)
            startActivity(intent)
        }
        binding.scoreBoard.setOnClickListener {
            val intent = Intent(context, ScoreBoard::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val pointsOverall = sharedPreferences.getInt(Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS, 0)
        binding.score.text = pointsOverall.toString()
    }


}