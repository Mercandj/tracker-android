package com.mercandalli.tracker.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mercandalli.tracker.R
import com.mercandalli.tracker.common.Preconditions

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        setSupportActionBar(findViewById(R.id.activity_permission_toolbar))

    }
}
