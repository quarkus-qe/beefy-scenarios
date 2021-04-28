package io.quarkus.qe.rest.data;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, Long> {

    //This will override behaviour of HTTP GET method of PanacheResource
    //TODO change to findAll() once feature is implemented https://github.com/quarkusio/quarkus/issues/16871
    @Override
    public PanacheQuery<UserEntity> findAll(Sort sort) {
        return find("select u from user_entity u order by u.name asc");
    }
}
