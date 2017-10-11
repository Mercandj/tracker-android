package com.mercandalli.tracker.main_thread

import android.os.Handler

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Test class for the [MainThreadPostImpl]
 */
class MainThreadPostImplTest {

    /**
     * To test.
     */
    private var mainThreadPostImpl: MainThreadPostImpl? = null

    @Mock
    private val mainThreadHandler: Handler? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mainThreadPostImpl = MainThreadPostImpl(
                Thread.currentThread(),
                mainThreadHandler!!)
    }

    @Test
    fun isOnMainThread() {
        Assert.assertTrue(mainThreadPostImpl!!.isOnMainThread)
    }
}
