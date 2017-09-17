package com.mercandalli.tracker.scheduler

interface SchedulerPeriodicTrigger {

    fun setup()

    fun onTriggered()

    fun registerScheduleListener(listener: ScheduleListener)

    fun unregisterScheduleListener(listener: ScheduleListener)

    interface ScheduleListener {

        fun onTriggered()
    }
}
