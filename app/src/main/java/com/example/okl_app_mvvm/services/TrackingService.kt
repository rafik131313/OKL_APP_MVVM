package com.example.okl_app_mvvm.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Build
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.okl_app_mvvm.other.Constants.ACTION_STOP_SERVICE
import com.example.okl_app_mvvm.other.Constants.ANNEXE_COORDINATES
import com.example.okl_app_mvvm.other.Constants.BISONS_COORDINATES
import com.example.okl_app_mvvm.other.Constants.CASTLE_COORDINATES
import com.example.okl_app_mvvm.other.Constants.FASTEST_LOCATION_INTERVAL
import com.example.okl_app_mvvm.other.Constants.KEY_SHAREDPREFERENCES_VISITING_ANNEXE
import com.example.okl_app_mvvm.other.Constants.KEY_SHAREDPREFERENCES_VISITING_BISONS
import com.example.okl_app_mvvm.other.Constants.KEY_SHAREDPREFERENCES_VISITING_CASTLE
import com.example.okl_app_mvvm.other.Constants.KEY_SHARED_PREFERENCES_OVERALLPOINTS
import com.example.okl_app_mvvm.other.Constants.LOCATION_UPDATE_INTERVAL
import com.example.okl_app_mvvm.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.okl_app_mvvm.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.okl_app_mvvm.other.Constants.NOTIFICATION_ID
import com.example.okl_app_mvvm.other.TrackingUtility
import com.example.okl_app_mvvm.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import hilt_aggregated_deps._com_example_okl_app_mvvm_ui_fragments_AnimalsFragment_GeneratedInjector
import javax.inject.Inject


@AndroidEntryPoint
class TrackingService : LifecycleService() {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var sharedPreferences: SharedPreferences



    lateinit var curNotificationBuilder: NotificationCompat.Builder



    companion object {
        val isTracking = MutableLiveData<Boolean>()
    }


    private fun postInitialValues(){
        isTracking.postValue(false)
    }

    override fun onCreate() {
        super.onCreate()
        curNotificationBuilder = baseNotificationBuilder
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE ->{
                    startForegroundService()
                    isTracking.postValue(true)
                }
                ACTION_STOP_SERVICE ->{
                    killService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean){
        if(isTracking){
            if (TrackingUtility.hasLocationPermissions(this)){
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value!!){
                result?.locations.let { locations ->
                    for(location in locations){
                        checkCoordinates(location)
                    }
                }
            }
        }
    }

    private fun killService(){
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    private fun pauseService(){
        isTracking.postValue(false)
    }

    private fun checkCoordinates(location : Location?){
        location?.let {
            val pos =
                com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude)
            val longitude = pos.longitude
            val latitude = pos.latitude
            if (latitude in CASTLE_COORDINATES.a..CASTLE_COORDINATES.b &&
                longitude in CASTLE_COORDINATES.c..CASTLE_COORDINATES.d
            ) {
                val castle =
                    sharedPreferences.getBoolean(KEY_SHAREDPREFERENCES_VISITING_CASTLE, false)
                if (!castle) {
                    getAndAddOverallPoints()
                    sharedPreferencesAdd(KEY_SHAREDPREFERENCES_VISITING_CASTLE, true)
                }
            }
            if (latitude in ANNEXE_COORDINATES.a..ANNEXE_COORDINATES.b &&
                longitude in ANNEXE_COORDINATES.c..ANNEXE_COORDINATES.d
            ) {
                val annexe =
                    sharedPreferences.getBoolean(KEY_SHAREDPREFERENCES_VISITING_ANNEXE, false)
                if (!annexe) {
                    getAndAddOverallPoints()
                    sharedPreferencesAdd(KEY_SHAREDPREFERENCES_VISITING_ANNEXE, true)
                }
            }
            if (latitude in BISONS_COORDINATES.a..BISONS_COORDINATES.b &&
                longitude in BISONS_COORDINATES.c..BISONS_COORDINATES.d
            ) {
                val bisons =
                    sharedPreferences.getBoolean(KEY_SHAREDPREFERENCES_VISITING_BISONS, false)
                if (!bisons) {
                    getAndAddOverallPoints()
                    sharedPreferencesAdd(KEY_SHAREDPREFERENCES_VISITING_BISONS, true)
                }
            }
        }
    }

    private fun getAndAddOverallPoints(){
        var pointsOverall = sharedPreferences.getInt(KEY_SHARED_PREFERENCES_OVERALLPOINTS, 0)
        pointsOverall++
        sharedPreferences.edit()
            .putInt(KEY_SHARED_PREFERENCES_OVERALLPOINTS, pointsOverall)
            .apply()
    }

    private fun sharedPreferencesAdd(key: String, boolean: Boolean){
        sharedPreferences.edit()
            .putBoolean(key, boolean)
            .apply()
    }

    private fun startForegroundService(){

        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }



        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}