package com.mercandalli.tracker.device

interface DeviceNicknameManager {

    fun getNickname(): String?

    fun setNickname(deviceNickname: String)
}