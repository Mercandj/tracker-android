package com.mercandalli.tracker.maps

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.mercandalli.tracker.R

class MapsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null

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
        }
    }
}
