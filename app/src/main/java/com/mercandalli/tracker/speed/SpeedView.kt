package com.mercandalli.tracker.speed

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.mercandalli.tracker.R
import com.mercandalli.tracker.location.Location
import com.mercandalli.tracker.location.LocationRepository
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.main.TrackerComponent

class SpeedView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val appComponent: TrackerComponent = TrackerApplication.appComponent
    private val locationManager = appComponent.provideLocationManager()
    private val locationRepository = appComponent.provideLocationRepository()
    private val locationRepositoryListener = createLocationRepositoryListener()

    private var textView: TextView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_speed, this)
        textView = findViewById(R.id.view_speed_text_view)

        findViewById<View>(R.id.view_speed_fab).setOnClickListener {
            locationManager.requestLocationUpdates()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        locationRepository.registerLocationRepositoryListener(locationRepositoryListener)
        locationManager.requestLocationUpdates()
        syncSpeedText()
    }

    override fun onDetachedFromWindow() {
        locationRepository.unregisterLocationRepositoryListener(locationRepositoryListener)
        locationManager.stopLocationUpdates()
        super.onDetachedFromWindow()
    }

    private fun createLocationRepositoryListener(): LocationRepository.LocationRepositoryListener {
        return object : LocationRepository.LocationRepositoryListener {
            override fun onLocationChanged() {
                syncSpeedText()
            }
        }
    }

    private fun syncSpeedText() {
        val location: Location? = locationRepository.getLocation()
        if (location == null) {
            Toast.makeText(context, "No location", Toast.LENGTH_SHORT).show()
        } else {
            val positions = locationRepository.getLocations().size
            textView!!.text = ("Positions: " + positions.toString() + "\n\n" + location.toString())
        }
    }
}
