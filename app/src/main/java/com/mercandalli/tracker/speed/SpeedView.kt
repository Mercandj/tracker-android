package com.mercandalli.tracker.speed

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.mercandalli.tracker.R
import com.mercandalli.tracker.location.CurrentLocation
import com.mercandalli.tracker.location.CurrentLocationManager
import com.mercandalli.tracker.main.TrackerApplication
import com.mercandalli.tracker.main.TrackerComponent

class SpeedView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val appComponent: TrackerComponent = TrackerApplication.appComponent
    private val currentLocationManager = appComponent.provideCurrentLocationManager()
    private val currentLocationListener = createCurrentLocationListener()

    private var textView: TextView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_speed, this)
        textView = findViewById(R.id.view_speed_text_view)

        findViewById<View>(R.id.view_speed_fab).setOnClickListener {
            currentLocationManager.requestSingleUpdate()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        currentLocationManager.registerCurrentLocationListener(currentLocationListener)
    }

    override fun onDetachedFromWindow() {
        currentLocationManager.unregisterCurrentLocationListener(currentLocationListener)
        super.onDetachedFromWindow()
    }

    private fun createCurrentLocationListener(): CurrentLocationManager.CurrentLocationListener {
        return object : CurrentLocationManager.CurrentLocationListener {
            override fun onCurrentLocationChanged(location: CurrentLocation?) {
                if (location == null) {
                    Toast.makeText(context, "No location", Toast.LENGTH_SHORT).show()
                } else {
                    textView!!.text = location.toString()
                }
            }
        }
    }
}
