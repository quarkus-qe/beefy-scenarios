package io.quarkus.qe;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;

public interface ApplicationResource extends PanacheEntityResource<ApplicationEntity, Long> {
}
