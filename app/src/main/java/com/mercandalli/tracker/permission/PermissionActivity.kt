package com.mercandalli.tracker.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.mercandalli.tracker.R
import com.mercandalli.tracker.common.Preconditions
import com.mercandalli.tracker.main.MainApplication

class PermissionActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            Preconditions.checkNotNull(context)
            val intent = Intent(context, PermissionActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }

    private var applicationsStatsTextView: TextView? = null
    private val appComponent = MainApplication.appComponent
    private val deviceApplicationManager = appComponent.provideDeviceApplicationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        setSupportActionBar(findViewById(R.id.activity_permission_toolbar))

        applicationsStatsTextView = findViewById(R.id.activity_permission_applications_stats_text)
        syncApplicationsStatsTextView()

        findViewById<View>(R.id.activity_permission_applications_stats_card).setOnClickListener {
            deviceApplicationManager.requestUsagePermission()
        }
    }

    override fun onResume() {
        super.onResume()
        syncApplicationsStatsTextView()
    }

    private fun syncApplicationsStatsTextView() {
        applicationsStatsTextView!!.text = if (deviceApplicationManager.needUsageStatsPermission()) {
            getString(R.string.activity_permission_applications_stats_disable)
        } else {
            getString(R.string.activity_permission_applications_stats_enable)
        }
    }
}
