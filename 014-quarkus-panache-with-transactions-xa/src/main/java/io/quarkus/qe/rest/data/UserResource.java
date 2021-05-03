package io.quarkus.qe.rest.data;

import java.util.List;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.rest.data.panache.MethodProperties;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(hal = true, halCollectionName = "user_list", path = "users")
public interface UserResource extends PanacheRepositoryResource<UserRepository, UserEntity, Long> {

    @MethodProperties(path = "all")
    List<UserEntity> list(Page page, Sort sort);

    @MethodProperties(exposed = false)
    UserEntity update(Long id, UserEntity entity);
}
