package io.quarkus.qe.hibernate.transaction;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.TransactionScoped;

@TransactionScoped
public class TransactionScopeBean {
    public static final AtomicBoolean PRE_DESTROY_INVOKED = new AtomicBoolean(false);
    public static final AtomicBoolean POST_CONSTRUCT_INVOKED = new AtomicBoolean(false);

    private int value = 0;

    public int increment() {
        return ++value;
    }

    @PostConstruct
    void postConstruct() {
        POST_CONSTRUCT_INVOKED.set(true);
    }

    @PreDestroy
    void preDestroy() {
        PRE_DESTROY_INVOKED.set(true);
    }

    public static void resetFlags() {
        PRE_DESTROY_INVOKED.set(false);
        POST_CONSTRUCT_INVOKED.set(false);
    }
}
