package io.quarkus.qe.vertx.sql;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.qe.vertx.sql.services.DbPoolService;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.pgclient.PgPool;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

/** Application is used as a main class in order to setup some global configuration */
@ApplicationScoped
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @ConfigProperty(name = "app.selected.db")
    String selectedDB;

    @ConfigProperty(name = "quarkus.flyway.schemas")
    String postgresqlDbName;

    @ConfigProperty(name = "quarkus.flyway.mysql.schemas")
    String mysqlDbName;

    @Inject
    PgPool postgresql;

    @Inject
    @Named("mysql")
    @IfBuildProfile("mysql")
    MySQLPool mysql;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting with profiles " + ConfigUtils.getProfiles());

        ObjectMapper mapper = DatabindCodec.mapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    @Singleton
    @Produces
    @Named("sqlClient")
    synchronized DbPoolService pool() {
        switch (selectedDB) {
            case "mysql":
                return new DbPoolService(mysql, mysqlDbName, selectedDB);
            default:
                return new DbPoolService(postgresql, postgresqlDbName, selectedDB);
        }
    }
}
