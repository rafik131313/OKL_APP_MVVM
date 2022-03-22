package com.example.okl_app_mvvm.ui.fragments

import android.Manifest
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.FragmentVisitingParkBinding
import com.example.okl_app_mvvm.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.okl_app_mvvm.other.Constants.ACTION_STOP_SERVICE
import com.example.okl_app_mvvm.other.Constants.KEY_SHAREDPREFERENCES_VISITING_ANNEXE
import com.example.okl_app_mvvm.other.Constants.KEY_SHAREDPREFERENCES_VISITING_BISONS
import com.example.okl_app_mvvm.other.Constants.KEY_SHAREDPREFERENCES_VISITING_CASTLE
import com.example.okl_app_mvvm.other.Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS
import com.example.okl_app_mvvm.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.okl_app_mvvm.other.TrackingUtility
import com.example.okl_app_mvvm.services.TrackingService
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class VisitingParkFragment : Fragment(R.layout.fragment_visiting_park), EasyPermissions.PermissionCallbacks {


    private var _binding: FragmentVisitingParkBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences


    lateinit var mainHandler: Handler
    private var isTracking = false

    private val updateTextTask = object : Runnable {
        override fun run() {
            changeUiAndGetSharedPref()
            mainHandler.postDelayed(this, 4000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVisitingParkBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions()
        subscribeToObservers()
        changeUiAndGetSharedPref()
        mainHandler = Handler(Looper.getMainLooper())

        binding.turnOnTracking.setOnClickListener{
            toggleTracking()
        }
    }

/*    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateTextTask)
        Toast.makeText(context, "onPause", Toast.LENGTH_SHORT).show()
    }*/

    private fun changeUiAndGetSharedPref(){

        val pointsOverall = sharedPreferences.getInt(KEY_SHARED_PREFERENCES_OVERALLPOINTS, 0)
        val castleDone = sharedPreferences.getBoolean(KEY_SHAREDPREFERENCES_VISITING_CASTLE, false)
        val annexeDone = sharedPreferences.getBoolean(KEY_SHAREDPREFERENCES_VISITING_ANNEXE, false)
        val bisonsDone = sharedPreferences.getBoolean(KEY_SHAREDPREFERENCES_VISITING_BISONS, false)
        binding.score.text = pointsOverall.toString()

        ifPointIsAddedChangeColor(castleDone, annexeDone, bisonsDone)
    }

    private fun ifPointIsAddedChangeColor(castleDone: Boolean, annexeDone: Boolean, bisonsDone: Boolean){
        if (castleDone){
            binding.checkCastle.background = ContextCompat.getDrawable(requireContext(), R.drawable.picslayout)
            binding.castleEntry.background = ContextCompat.getDrawable(requireContext(), R.drawable.picslayout2)
        }
        if(annexeDone) {
            binding.annexe.background = ContextCompat.getDrawable(requireContext(), R.drawable.picslayout)
            binding.checkAnnexe.background = ContextCompat.getDrawable(requireContext(), R.drawable.picslayout2)
        }
        if(bisonsDone) {
            binding.bisons.background = ContextCompat.getDrawable(requireContext(), R.drawable.picslayout)
            binding.checkBisons.background = ContextCompat.getDrawable(requireContext(), R.drawable.picslayout2)
        }
    }

    private fun subscribeToObservers(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })
    }




    private fun updateTracking(isTracking: Boolean){
        this.isTracking = isTracking
        if(!isTracking){
            binding.whatToDoTracking.text = resources.getString(R.string.instrukcja_visiting)
            binding.turnOnTracking.text = resources.getString(R.string.turnon)
            binding.turnOnTracking.background = resources.getDrawable(R.drawable.button_switch_on)
        }
        else{
            binding.whatToDoTracking.text = getString(R.string.sightseeing)
            binding.turnOnTracking.text = resources.getString(R.string.turnoff)
            binding.turnOnTracking.background = resources.getDrawable(R.drawable.button_switch_off)
        }
    }

    private fun toggleTracking(){
        if (isTracking){
            sendCommandToService(ACTION_STOP_SERVICE)
            mainHandler.removeCallbacks(updateTextTask)
        }else
        {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            mainHandler.post(updateTextTask)
        }
    }


    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }




    private fun requestPermissions() {
        when {
            TrackingUtility.hasLocationPermissions(requireContext()) -> {
                return
            }
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            else -> {
                EasyPermissions.requestPermissions(
                    this,
                    resources.getString(R.string.Permission),
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
        }
    }
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}