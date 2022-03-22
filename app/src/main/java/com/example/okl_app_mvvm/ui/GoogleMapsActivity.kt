package com.example.okl_app_mvvm.ui

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.okl_app_mvvm.R
import com.example.okl_app_mvvm.databinding.ActivityGoogleMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding

    private val TAG = GoogleMapsActivity::class.java.simpleName
    private val LOCATION_PERMISSION_REQUEST = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationAccess()

        val polylineOptions = PolygonOptions()
            .add(LatLng(51.852109, 17.929249))
            .add(
                LatLng(
                    51.856366,
                    17.926934
                )
            ) // North of the previous point, but at the same longitude
            .add(LatLng(51.857853, 17.928534)) // Same latitude, and 30km to the west
            .add(LatLng(51.858875, 17.928557)) // Same longitude, and 16km to the south
            .add(LatLng(51.858265, 17.924368))
            .add(LatLng(51.857980, 17.924341))
            .add(LatLng(51.858411, 17.923032))
            .add(LatLng(51.861737, 17.923601))
            .add(LatLng(51.861509, 17.928622))
            .add(LatLng(51.863848, 17.928513))
            .add(LatLng(51.864408, 17.928657))
            .add(LatLng(51.866294, 17.930140))
            .add(LatLng(51.868177, 17.931039))
            .add(LatLng(51.868652, 17.931303))
            .add(LatLng(51.869330, 17.931246))
            .add(LatLng(51.871731, 17.930435))
            .add(LatLng(51.872170, 17.930514))
            .add(LatLng(51.872612, 17.930675))
            .add(LatLng(51.874023, 17.918493))
            .add(LatLng(51.861911, 17.906923))
            .add(LatLng(51.856985, 17.916779))
            .add(LatLng(51.855854, 17.919682)) //droga
            .add(LatLng(51.852185, 17.929130))
            .add(LatLng(51.852109, 17.929249))
            .strokeColor(getColor(R.color.default_map))
            .fillColor(getColor(R.color.default_map))

        val parking = LatLng(51.851082, 17.937267)
        val castle = LatLng(51.852704, 17.933824)
        val farm = LatLng(51.859122, 17.924695)
        val office = LatLng(51.851475, 17.932647)
        val powozownia = LatLng(51.852927, 17.935830)
        val oficyna = LatLng(51.851378, 17.935282)

        setMapStyle(mMap)
        mMap.addPolygon(polylineOptions)

        mMap.addMarker(
            MarkerOptions().position(parking).title(getString(R.string.parking)).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.parking)
            )
        )
        mMap.addMarker(
            MarkerOptions().position(castle).title(getString(R.string.castle)).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.castle)
            )
        )
        mMap.addMarker(
            MarkerOptions().position(farm).title(getString(R.string.farm)).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.farm)
            )
        )
        mMap.addMarker(
            MarkerOptions().position(powozownia).title(getString(R.string.coach_house)).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.coach)
            )
        )
        mMap.addMarker(
            MarkerOptions().position(office).title(getString(R.string.offices)).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.offices)
            )
        )
        mMap.addMarker(
            MarkerOptions().position(oficyna).title(getString(R.string.annexe)).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.annexe)
            )
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(castle, 16f))

        mMap.setMinZoomPreference(15.2f)

        val adelaideBounds = LatLngBounds(
            LatLng(51.849267, 17.925767),  // SW bounds
            LatLng(51.872864, 17.938552) // NE bounds
        )

        mMap.setLatLngBoundsForCameraTarget(adelaideBounds)


    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.mapstyle
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    this,
                    "User has not granted location access permission",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }
}