package com.mercandalli.tracker.main_thread

import android.os.Handler
import com.mercandalli.tracker.common.Preconditions

/**
 * Simple [MainThreadPost] implementation.
 */
class MainThreadPostImpl(

        /**
         * The main (UI) [Thread] used to be sure that the callbacks are on the main [Thread].
         */
        private val mainThread: Thread,
        /**
         * An [Handler] used to be sure that the callbacks are on the main [Thread].
         */
        private val mainThreadHandler: Handler) : MainThreadPost {

    init {
        Preconditions.checkNotNull(mainThread)
        Preconditions.checkNotNull(mainThreadHandler)
    }

    override val isOnMainThread: Boolean get() = Thread.currentThread() === mainThread

    override fun post(runnable: Runnable) {
        mainThreadHandler.post(runnable)
    }
}
