package com.example.okl_app_mvvm.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.FragmentAboutBinding
import com.example.okl_app_mvvm.databinding.FragmentAnimalScreenBinding

class AnimalScreen : AppCompatActivity() {

    private lateinit var binding: FragmentAnimalScreenBinding
    private var mp: MediaPlayer? = null
    var already_played = R.raw.boar_sound

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentAnimalScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animal_picked = intent.getStringExtra("intent_animal")

        var picture_taken = ContextCompat.getDrawable(this, R.drawable.sarna)

        if (animal_picked == "zubr") {
            playSound(R.raw.wisent_sound)
            mp?.setLooping(true)
            picture_taken = ContextCompat.getDrawable(this, R.drawable.zubr_okl)
            already_played = R.raw.wisent_sound
            binding.animalDiscription.setText(R.string.Zubry)
            binding.TVAnimalName.setText(R.string.zubr)
        }
        else if (animal_picked == "konik") {
            playSound(R.raw.horse_sound)
            mp?.setLooping(true)
            picture_taken = ContextCompat.getDrawable(this, R.drawable.konik_okl)
            already_played = R.raw.horse_sound
            binding.animalDiscription.setText(R.string.Koniki)
            binding.TVAnimalName.setText(R.string.konik)
        }
        else if (animal_picked == "dzik") {
            playSound(R.raw.boar_sound)
            mp?.setLooping(true)
            picture_taken = ContextCompat.getDrawable(this, R.drawable.dzik_okl)
            already_played = R.raw.boar_sound
            binding.animalDiscription.setText(R.string.Dziki)
            binding.TVAnimalName.setText(R.string.dzik)
        }
        else if (animal_picked == "sarna") {
            playSound(R.raw.roe_sound)
            mp?.setLooping(true)
            picture_taken = ContextCompat.getDrawable(this, R.drawable.daniel_okl2)
            already_played = R.raw.roe_sound
            binding.animalDiscription.setText(R.string.Daniele)
            binding.TVAnimalName.setText(R.string.daniel)
        }


        binding.pictureAnimals.setImageDrawable(picture_taken)

        var blind = false

        binding.animalDiscription.setOnClickListener{
            if (blind == false){
                binding.animalDiscription.textSize = 30F
                blind = true
            }
            else
            {
                binding.animalDiscription.textSize = 20F
                blind = false
            }
        }


        binding.floating.setOnClickListener(){
            if (mp == null)
            {
                playSound(already_played)
                binding.floating.setImageResource(R.drawable.ic_baseline_stop_circle_24)

            }
            else
            {
                stopSound()
                binding.floating.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        stopSound()
        binding.floating.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopSound()
    }



    private fun playSound(id: Int){
        if(mp == null){
            mp = MediaPlayer.create(this, id)
        }
        mp?.start()
    }
    private fun stopSound(){
        mp?.stop()
        mp?.reset()
        mp?.release()
        mp = null
    }
}