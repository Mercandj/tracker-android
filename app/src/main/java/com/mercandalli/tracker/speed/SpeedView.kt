package com.mercandalli.tracker.speed

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.mercandalli.tracker.R

class SpeedView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_speed, this)

    }
}
