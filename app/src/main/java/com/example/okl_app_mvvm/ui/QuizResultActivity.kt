package com.example.okl_app_mvvm.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.ActivityQuizResultBinding
import com.example.okl_app_mvvm.other.Constants
import com.example.okl_app_mvvm.other.Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS
import com.example.okl_app_mvvm.other.Constants.KEY_WAS_PLAYED_QUIZ
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuizResultActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var binding: ActivityQuizResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pointsOverall = sharedPreferences.getInt(Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS, 0)
        val was_played_quiz = sharedPreferences.getBoolean(KEY_WAS_PLAYED_QUIZ, false)

        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)
        val correctAnswers = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)

        binding.tvScore.text = resources.getString(R.string.rightAnsw) + " " + correctAnswers + "/" + totalQuestions + " " + resources.getString(R.string.gjPoints)
        if (was_played_quiz == false) {
            pointsOverall+=correctAnswers
            sharedPreferences.edit()
                .putInt(KEY_SHARED_PREFERENCES_OVERALLPOINTS, pointsOverall)
                .putBoolean(KEY_WAS_PLAYED_QUIZ, true)
                .apply()
        }
        else
        {
            Toast.makeText(this, resources.getString(R.string.pointsWontBeAdded), Toast.LENGTH_SHORT).show()
        }

        binding.btnFinish.setOnClickListener {
            finish()
            startActivity(Intent(this@QuizResultActivity, MainActivity::class.java))

        }

    }
}