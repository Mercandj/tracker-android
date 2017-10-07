package com.mercandalli.tracker.root

internal class RootManagerImpl : RootManager {
    override fun isRooted(): Boolean {
        return RootUtils.isRooted
    }
}