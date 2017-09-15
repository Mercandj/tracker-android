package com.mercandalli.tracker.main_thread;

import android.os.Handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for the {@link MainThreadPostImpl}
 */
public class MainThreadPostImplTest {

    /**
     * To test.
     */
    private MainThreadPostImpl mainThreadPostImpl;

    @Mock
    private Handler mainThreadHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mainThreadPostImpl = new MainThreadPostImpl(
                Thread.currentThread(),
                mainThreadHandler);
    }

    @Test
    public void isOnMainThread() {
        Assert.assertTrue(mainThreadPostImpl.isOnMainThread());
    }
}
