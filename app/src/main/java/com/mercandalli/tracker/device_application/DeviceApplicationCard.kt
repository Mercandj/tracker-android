package com.mercandalli.tracker.device_application

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mercandalli.tracker.R
import java.util.concurrent.TimeUnit


class DeviceApplicationCard @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val icon: ImageView
    private val title: TextView
    private val subtitle: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_device_application_card, this)
        orientation = LinearLayout.VERTICAL
        icon = findViewById(R.id.view_application_card_icon)
        title = findViewById(R.id.view_application_card_title)
        subtitle = findViewById(R.id.view_application_card_subtitle)
    }

    internal fun setDeviceApplication(deviceApplications: DeviceApplication) {
        icon.setImageDrawable(deviceApplications.icon)
        title.text = deviceApplications.androidAppName
        subtitle.text = getDurationBreakdown(deviceApplications.totalTimeInForeground)
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    fun getDurationBreakdown(millis: Long): String {
        var millis = millis
        if (millis < 0) {
            throw IllegalArgumentException("Duration must be greater than zero!")
        }

        val days = TimeUnit.MILLISECONDS.toDays(millis)
        millis -= TimeUnit.DAYS.toMillis(days)
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)

        val sb = StringBuilder(64)
        if (days > 0) {
            sb.append(days)
            sb.append("d ")
        }
        if (hours > 0) {
            sb.append(hours)
            sb.append("h ")
        }
        if (days <= 0) {
            sb.append(minutes)
            sb.append("m ")
            sb.append(seconds)
            sb.append("s")
        }
        return sb.toString()
    }
}