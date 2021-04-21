package io.quarkus.qe.vertx.sql.dbpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCommons {

    protected CountDownLatch latch;
    private volatile boolean awaitCalled;

    public void await(long delay, TimeUnit timeUnit) {
        if (awaitCalled) {
            throw new IllegalStateException("await() already called");
        }
        awaitCalled = true;
        try {
            boolean ok = latch.await(delay, timeUnit);
            if (!ok) {
                // timed out
                throw new IllegalStateException("Timed out in waiting for test complete");
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException("Test thread was interrupted!");
        }
    }
}
