package io.quarkus.qe.rest.data;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, Long> {

    @Override
    public PanacheQuery<UserEntity> findAll() {
        return find("select u from user_entity u order by u.name asc");
    }
}
