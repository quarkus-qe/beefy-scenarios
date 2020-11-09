package org.acme.quickstart;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import io.quarkus.runtime.StartupEvent;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InfinispanClientApp {

    private static final Logger LOGGER = Logger.getLogger(InfinispanClientApp.class);

    @Inject
    RemoteCacheManager cacheManager;

    private static final String CACHE_CONFIG = "<infinispan><cache-container>" +
            "<distributed-cache name=\"mycache\"></distributed-cache>" +
            "</cache-container></infinispan>";

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Create or get cache named mycache with the default configuration");
        RemoteCache<Object, Object> cache = cacheManager.administration().getOrCreateCache("mycache",
                new XMLStringConfiguration(CACHE_CONFIG));
        cache.put("hello", "Hello World, Infinispan is up!");
    }
}
