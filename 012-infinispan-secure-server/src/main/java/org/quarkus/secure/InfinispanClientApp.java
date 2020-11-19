package org.quarkus.secure;

import io.quarkus.runtime.StartupEvent;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class InfinispanClientApp {

    private static final Logger LOGGER = Logger.getLogger(InfinispanClientApp.class);

    @Inject
    RemoteCacheManager cacheManager;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Get cache named 'default'");
        RemoteCache<Object, Object> cache = cacheManager.getCache("default");
        cache.put("hello", "Hello World, Infinispan is up!");
    }
}