package com.mercandalli.tracker.maps

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mercandalli.tracker.R
import com.mercandalli.tracker.location.LocationRepository
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.main.TrackerComponent

class MapsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null

    private val appComponent: TrackerComponent = TrackerApplication.appComponent
    private val locationManager = appComponent.provideLocationManager()
    private val locationRepository = appComponent.provideLocationRepository()
    private val locationRepositoryListener = createLocationRepositoryListener()

    private val markers: MutableList<Marker> = ArrayList()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_maps, this)
        val supportFragmentManager = (context as AppCompatActivity).supportFragmentManager

        mapFragment = supportFragmentManager.findFragmentById(R.id.view_maps_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(createOnMapReadyCallback())

        findViewById<View>(R.id.view_maps_fab).setOnClickListener({

        })
    }

    private fun createOnMapReadyCallback(): OnMapReadyCallback {
        return OnMapReadyCallback {
            this.googleMap = googleMap
            syncMarkers()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        locationRepository.registerLocationRepositoryListener(locationRepositoryListener)
        locationManager.requestLocationUpdates()
        syncMarkers()
    }

    override fun onDetachedFromWindow() {
        locationRepository.unregisterLocationRepositoryListener(locationRepositoryListener)
        locationManager.stopLocationUpdates()
        super.onDetachedFromWindow()
    }

    private fun createLocationRepositoryListener(): LocationRepository.LocationRepositoryListener {
        return object : LocationRepository.LocationRepositoryListener {
            override fun onLocationChanged() {
                syncMarkers()
            }
        }
    }

    private fun syncMarkers() {
        if (googleMap == null) {
            return
        }
        for (marker in markers) {
            marker.remove()
        }
        val locations = locationRepository.getLocations()
        for (location in locations) {
            val title = "" + location.timestamp
            val marker = googleMap!!.addMarker(MarkerOptions()
                    .position(location.toLatLng())
                    .title(title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
            markers.add(marker)
        }

        val location = locationRepository.getLocation()
        if (location != null) {
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location.toLatLng(), 18f))
        }
    }
}
