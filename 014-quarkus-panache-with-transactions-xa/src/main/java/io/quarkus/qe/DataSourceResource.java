package io.quarkus.qe;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("/data-source")
public class DataSourceResource {
    @Inject
    AgroalDataSource defaultDataSource;

    @Inject
    @DataSource("with-xa")
    AgroalDataSource xaDataSource;

    @GET
    @Path("/default/connection-provider-class")
    public String defaultConnectionProviderClass() {
        return getConnectionProviderClass(defaultDataSource);
    }

    @GET
    @Path("/with-xa/connection-provider-class")
    public String withXaConnectionProviderClass() {
        return getConnectionProviderClass(xaDataSource);
    }

    private String getConnectionProviderClass(AgroalDataSource dataSource) {
        return dataSource.getConfiguration().connectionPoolConfiguration()
                .connectionFactoryConfiguration().connectionProviderClass().getName();
    }
}
