package com.mercandalli.tracker.user

interface UserManager {

    fun sendDeviceSpecs()

    fun getDeviceSpecs(userId: String)
}