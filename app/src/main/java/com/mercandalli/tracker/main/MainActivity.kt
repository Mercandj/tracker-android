package com.mercandalli.tracker.main

import android.Manifest
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.mercandalli.tracker.R
import com.mercandalli.tracker.device.DeviceView
import com.mercandalli.tracker.maps.MapsView

class MainActivity : AppCompatActivity() {

    /**
     * A request code for requesting the permissions needed by this application.
     */
    private val PERMISSION_REQUEST_CODE = 42

    private val KEY_CURRENT_VIEW = "current_view"

    private val PERMISSION_REQUIRED = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION)

    private var container: ViewGroup? = null
    private var speedView: DeviceView? = null
    private var mapsView: MapsView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.activity_main_toolbar))

        speedView = DeviceView(this)
        container = findViewById(R.id.activity_main_container)
        container?.addView(speedView)

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.action_ongle_0 -> onBottomBarSpeedClicked()
                        R.id.action_ongle_1 -> onBottomBarMapsClicked()
                        else -> throw IllegalStateException("Wrong id")
                    }
                    return@OnNavigationItemSelectedListener true
                })

        ActivityCompat.requestPermissions(this, PERMISSION_REQUIRED, PERMISSION_REQUEST_CODE)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val childAt = container!!.getChildAt(0)
        when (childAt) {
            is DeviceView -> outState!!.putInt(KEY_CURRENT_VIEW, 0)
            is MapsView -> outState!!.putInt(KEY_CURRENT_VIEW, 1)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val i = savedInstanceState!!.getInt(KEY_CURRENT_VIEW)
        when (i) {
            0 -> onBottomBarSpeedClicked()
            1 -> onBottomBarMapsClicked()
        }
    }

    private fun onBottomBarSpeedClicked() {
        if (speedView == null) {
            speedView = DeviceView(this)
        }
        mapsView?.removeMap()
        container?.removeAllViews()
        container?.addView(speedView)
    }

    private fun onBottomBarMapsClicked() {
        if (mapsView == null) {
            mapsView = MapsView(this)
        }
        container?.removeAllViews()
        container?.addView(mapsView)
    }
}