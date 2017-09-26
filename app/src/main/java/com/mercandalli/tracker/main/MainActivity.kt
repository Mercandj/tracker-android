package com.mercandalli.tracker.main

import android.Manifest
import android.annotation.TargetApi
import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Rational
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mercandalli.tracker.R
import com.mercandalli.tracker.device.DeviceView
import com.mercandalli.tracker.device_specs.DeviceCpuView
import com.mercandalli.tracker.maps.MapsView
import com.mercandalli.tracker.permission.PermissionActivity

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
    private var deviceView: DeviceView? = null
    private var mapsView: MapsView? = null
    private var deviceCpuView: DeviceCpuView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.activity_main_toolbar))

        deviceView = DeviceView(this)
        container = findViewById(R.id.activity_main_container)
        container?.addView(deviceView)

        findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation).setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.action_ongle_0 -> onBottomBarYouClicked()
                        R.id.action_ongle_1 -> onBottomBarTargetClicked()
                        //R.id.action_ongle_2 -> onBottomBarMapsClicked()
                        else -> throw IllegalStateException("Wrong id")
                    }
                    return@OnNavigationItemSelectedListener true
                })

        //ActivityCompat.requestPermissions(this, PERMISSION_REQUIRED, PERMISSION_REQUEST_CODE)
    }

    override fun onResume() {
        super.onResume()
        TrackerApplication.appComponent.provideDeviceApplicationManager().needUsageStatsPermission()
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
            0 -> onBottomBarYouClicked()
            1 -> onBottomBarMapsClicked()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.menu_main_item_picture_in_picture) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                minimize()
            }
            return true
        }
        if (itemId == R.id.menu_main_item_permissions) {
            /*
            val deviceApplicationManager = TrackerApplication.appComponent.provideDeviceApplicationManager()
            if (deviceApplicationManager.needUsageStatsPermission()) {
                deviceApplicationManager.requestUsagePermission()
            }
            */
            PermissionActivity.start(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        val toolbarAndBottomBarVisibility = if (isInPictureInPictureMode) View.GONE else View.VISIBLE
        findViewById<View>(R.id.activity_main_toolbar).visibility = toolbarAndBottomBarVisibility
        findViewById<View>(R.id.activity_main_bottom_navigation).visibility = toolbarAndBottomBarVisibility

        if (isInPictureInPictureMode) {
            if (deviceCpuView == null) {
                deviceCpuView = DeviceCpuView(this)
            }
            findViewById<FrameLayout>(R.id.activity_main_root).addView(deviceCpuView,
                    FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT))
        } else {
            findViewById<FrameLayout>(R.id.activity_main_root).removeView(deviceCpuView)
        }
    }

    private fun onBottomBarYouClicked() {
        if (deviceView == null) {
            deviceView = DeviceView(this)
        }
        mapsView?.removeMap()
        container?.removeAllViews()
        container?.addView(deviceView)
    }

    private fun onBottomBarTargetClicked() {

    }

    private fun onBottomBarMapsClicked() {
        if (mapsView == null) {
            mapsView = MapsView(this)
        }
        container?.removeAllViews()
        container?.addView(mapsView)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun minimize() {
        val pictureInPictureParamsBuilder = PictureInPictureParams.Builder()
        val dimensionX = resources.getDimension(R.dimen.activity_main_pic_width).toInt()
        val dimensionY = resources.getDimension(R.dimen.activity_main_pic_height).toInt()
        val aspectRatio = Rational(dimensionX, dimensionY)
        pictureInPictureParamsBuilder.setAspectRatio(aspectRatio)
        enterPictureInPictureMode(pictureInPictureParamsBuilder.build())
    }
}