package com.mercandalli.tracker.common

import java.io.Closeable

internal object Closer {

    fun closeSilently(vararg xs: Closeable?) {
        xs.filterNotNull().forEach {
            try {
                it.close()
            } catch (ignored: Throwable) {
            }
        }
    }
}
