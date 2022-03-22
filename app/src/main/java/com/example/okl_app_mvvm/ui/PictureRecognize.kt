package com.example.okl_app_mvvm.ui

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.ActivityPictureRecognizeBinding
import com.example.okl_app_mvvm.ml.Model
import com.example.okl_app_mvvm.other.Constants.WHATS_ON_PHOTO
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import javax.inject.Inject

@AndroidEntryPoint
class PictureRecognize : AppCompatActivity() {

    lateinit var binding: ActivityPictureRecognizeBinding

    lateinit var bitmap: Bitmap

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private fun checkAndGetPermissions(){
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
        }
        else{
            Log.e("permissions", "GRANTED CAMERA")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                binding.camerabtn.visibility = View.VISIBLE
            }
            else{
                binding.permsInfo.visibility = View.VISIBLE
                binding.camerabtn.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureRecognizeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var picture = R.drawable.annexe

        val photo_picked = intent.getStringExtra("intent_photo")

        if (photo_picked == "Zamek")
        {
            picture = R.drawable.zamek_mine_img
        }
        else if (photo_picked == "Oficyna")
        {
            picture = R.drawable.annexe_img
        }
        else if (photo_picked == "Yellow")
        {
            picture = R.drawable.yellow_img
        }
        else if (photo_picked == "Wood_tractor")
        {
            picture = R.drawable.tractor_img
        }
        else if (photo_picked == "Totem")
        {
            picture = R.drawable.totem_img
        }
        else if (photo_picked == "Mask")
        {
            picture = R.drawable.mask_img
        }

        binding.imageView2.setImageResource(picture)

        checkAndGetPermissions()

        binding.btnConfirm.visibility = View.GONE
        binding.btnMakePrediction.visibility = View.GONE

        val labels = application.assets.open("labels.txt").bufferedReader().use { it.readText() }.split("\n")

        binding.imageView2.setOnClickListener{
            Log.d("mssg", "button pressed")
            var intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 250)
        }

        binding.btnMakePrediction.setOnClickListener {


            var resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val model = Model.newInstance(this)

            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            var max = getMax(outputFeature0.floatArray)

            val whats_on_photo = labels[max]

             sharedPreferences.edit()
                 .putString(WHATS_ON_PHOTO, whats_on_photo)
                 .apply()

/*            Toast.makeText(this, "${labels[max]}", Toast.LENGTH_SHORT).show()*/ //co rozpoznało

            if(labels[max] == photo_picked)
            {
                Toast.makeText(this, resources.getString(R.string.gj), Toast.LENGTH_SHORT).show()
                binding.btnMakePrediction.visibility = View.GONE
                binding.btnConfirm.visibility = View.VISIBLE
            }
            else
            {
                Toast.makeText(this, resources.getString(R.string.are_you_sure), Toast.LENGTH_SHORT).show()
                binding.btnConfirm.visibility = View.GONE
            }

            model.close()
        }

        binding.camerabtn.setOnClickListener{
            var camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera, 200)
        }

        binding.btnConfirm.setOnClickListener{
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 250 && resultCode == RESULT_OK){
            binding.imageView1.setImageURI(data?.data)

            var uri : Uri?= data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            binding.btnMakePrediction.visibility = View.VISIBLE
        }
        else if(requestCode == 200 && resultCode == RESULT_OK){
            bitmap = data?.extras?.get("data") as Bitmap
            binding.imageView1.setImageBitmap(bitmap)
            binding.camerabtn.text = resources.getString(R.string.picture_again)
            binding.btnMakePrediction.visibility = View.VISIBLE
        }

    }

    fun getMax(arr:FloatArray) : Int{
        var ind = 0;
        var min = 0.0f;

        for(i in 0..5)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                ind = i;
            }
        }
        return ind
    }
}