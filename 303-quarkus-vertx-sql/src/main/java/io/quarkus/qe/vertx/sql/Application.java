package io.quarkus.qe.vertx.sql;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.qe.vertx.sql.services.DbPoolService;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.mutiny.db2client.DB2Pool;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.pgclient.PgPool;

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

    @ConfigProperty(name = "quarkus.flyway.db2.schemas")
    String db2DbName;

    @Inject
    PgPool postgresql;

    @Inject
    @Named("mysql")
    @IfBuildProfile("mysql")
    MySQLPool mysql;

    @IfBuildProfile("db2")
    @Inject
    @Named("db2")
    DB2Pool db2;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting with profile " + ProfileManager.getActiveProfile());

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
            case "db2":
                return new DbPoolService(db2, "\"" + db2DbName + "\"", selectedDB);
            default:
                return new DbPoolService(postgresql, postgresqlDbName, selectedDB);
        }
    }
}
