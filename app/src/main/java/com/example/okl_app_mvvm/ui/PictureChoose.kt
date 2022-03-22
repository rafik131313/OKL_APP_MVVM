package com.example.okl_app_mvvm.ui

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.okl_app_mvvm.databinding.ActivityPictureChooseBinding
import com.example.okl_app_mvvm.other.Constants
import com.example.okl_app_mvvm.other.Constants.MASK
import com.example.okl_app_mvvm.other.Constants.OFICYNA
import com.example.okl_app_mvvm.other.Constants.PHOTO_RECOGNIZE_REQUEST_CODE
import com.example.okl_app_mvvm.other.Constants.TOTEM
import com.example.okl_app_mvvm.other.Constants.TRACTOR
import com.example.okl_app_mvvm.other.Constants.WHATS_ON_PHOTO
import com.example.okl_app_mvvm.other.Constants.YELLOW
import com.example.okl_app_mvvm.other.Constants.ZAMEK
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


var point = 0
var castle_done = false
var annexe_done = false
var yellow_done = false
var tractor_done = false
var totem_done = false
var mask_done = false

@AndroidEntryPoint
class PictureChoose : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var binding: ActivityPictureChooseBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        get_data()
        hide_file()

        val pointsOverall = sharedPreferences.getInt(Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS, 0)
        binding.score.text = pointsOverall.toString()

        binding.ibZamek.setOnClickListener {
            val intent = Intent(this, PictureRecognize::class.java)
            intent.putExtra("intent_photo", "Zamek")
            startActivityForResult(intent, PHOTO_RECOGNIZE_REQUEST_CODE)
        }
        binding.ibOficyna.setOnClickListener {
            val intent = Intent(this, PictureRecognize::class.java)
            intent.putExtra("intent_photo", "Oficyna")
            startActivityForResult(intent, PHOTO_RECOGNIZE_REQUEST_CODE)
        }
        binding.ibYellow.setOnClickListener {
            val intent = Intent(this, PictureRecognize::class.java)
            intent.putExtra("intent_photo", "Yellow")
            startActivityForResult(intent, PHOTO_RECOGNIZE_REQUEST_CODE)
        }
        binding.ibWoodTractor.setOnClickListener {
            val intent = Intent(this, PictureRecognize::class.java)
            intent.putExtra("intent_photo", "Wood_tractor")
            startActivityForResult(intent, PHOTO_RECOGNIZE_REQUEST_CODE)
        }
        binding.ibTotem.setOnClickListener {
            val intent = Intent(this, PictureRecognize::class.java)
            intent.putExtra("intent_photo", "Totem")
            startActivityForResult(intent, PHOTO_RECOGNIZE_REQUEST_CODE)
        }
        binding.ibMask.setOnClickListener {
            val intent = Intent(this, PictureRecognize::class.java)
            intent.putExtra("intent_photo", "Mask")
            startActivityForResult(intent, PHOTO_RECOGNIZE_REQUEST_CODE)
        }

        var blind = false
        binding.howToPlay.setOnClickListener{
            if (blind == false){
                binding.howToPlay.textSize = 30F
                blind = true
            }
            else
            {
                binding.howToPlay.textSize = 20F
                blind = false
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val whats_on_photo = sharedPreferences.getString(WHATS_ON_PHOTO, "Zamek")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PHOTO_RECOGNIZE_REQUEST_CODE && data != null) {
                if (whats_on_photo == ZAMEK) {
                    getAndAddOverallPoints()
                    sharedPreferences.edit()
                        .putBoolean(whats_on_photo, true)
                        .apply()
                } else if (whats_on_photo == OFICYNA) {
                    getAndAddOverallPoints()
                    sharedPreferences.edit()
                        .putBoolean(whats_on_photo, true)
                        .apply()
                } else if (whats_on_photo == YELLOW) {
                    getAndAddOverallPoints()
                    sharedPreferences.edit()
                        .putBoolean(whats_on_photo, true)
                        .apply()
                } else if (whats_on_photo == TRACTOR) {
                    getAndAddOverallPoints()
                    sharedPreferences.edit()
                        .putBoolean(whats_on_photo, true)
                        .apply()
                } else if (whats_on_photo == TOTEM) {
                    getAndAddOverallPoints()
                    sharedPreferences.edit()
                        .putBoolean(whats_on_photo, true)
                        .apply()
                }else if (whats_on_photo == MASK) {
                    getAndAddOverallPoints()
                    sharedPreferences.edit()
                        .putBoolean(whats_on_photo, true)
                        .apply()
                }
                get_data()
                hide_file()
            } else {
                Toast.makeText(
                    this,
                    "Wybrano niepoprawne zdjecie + $whats_on_photo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }


    fun hide_file(){
        if(castle_done) {
            binding.ibZamek.toGrayscale()
            binding.ibZamek.isClickable = false
        }
        if(annexe_done){
            binding.ibOficyna.toGrayscale()
            binding.ibOficyna.isClickable = false
        }
        if(yellow_done) {
            binding.ibYellow.toGrayscale()
            binding.ibYellow.isClickable = false
        }
        if(tractor_done) {
            binding.ibWoodTractor.toGrayscale()
            binding.ibWoodTractor.isClickable = false
        }
        if(totem_done) {
            binding.ibTotem.toGrayscale()
            binding.ibTotem.isClickable = false
        }
        if (mask_done) {
            binding.ibMask.toGrayscale()
            binding.ibMask.isClickable = false
        }
    }

    private fun getAndAddOverallPoints(){
        var pointsOverall = sharedPreferences.getInt(Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS, 0)
        pointsOverall++
        sharedPreferences.edit()
            .putInt(Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS, pointsOverall)
            .apply()
        binding.score.text = pointsOverall.toString()
    }


    fun get_data(){
        castle_done = sharedPreferences.getBoolean(ZAMEK, false)
        annexe_done = sharedPreferences.getBoolean(OFICYNA, false)
        yellow_done = sharedPreferences.getBoolean(YELLOW, false)
        tractor_done = sharedPreferences.getBoolean(TRACTOR, false)
        totem_done = sharedPreferences.getBoolean(TOTEM, false)
        mask_done = sharedPreferences.getBoolean(MASK, false)
    }

    fun ImageView.toGrayscale() {
        val matrix = ColorMatrix().apply {
            setSaturation(0f)
        }
        colorFilter = ColorMatrixColorFilter(matrix)
    }


}
