package org.smarthome.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CountDownLatchWaiter {

    private CountDownLatchWaiter() {}

    public static void awaitLatch(CountDownLatch latch) throws InterruptedException {
        if (Constants.debugModeActive()) {
            assertTrue(latch.await(10, TimeUnit.SECONDS));
        } else {
            latch.await();
        }
    }

}
