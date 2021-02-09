package io.quarkus.qe.vertx.sql.dbpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCommons {

    private volatile Throwable throwable;
    private volatile boolean awaitCalled;
    protected CountDownLatch latch;

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
            } else {
                rethrowError();
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException("Test thread was interrupted!");
        }
    }

    private void rethrowError() {
        if (throwable != null) {
            if (throwable instanceof Error) {
                throw (Error)throwable;
            } else if (throwable instanceof RuntimeException) {
                throw (RuntimeException)throwable;
            } else {
                throw new IllegalStateException(throwable);
            }

        }
    }
}
