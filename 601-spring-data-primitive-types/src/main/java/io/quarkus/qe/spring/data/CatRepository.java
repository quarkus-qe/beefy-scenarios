package io.quarkus.qe.spring.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import io.quarkus.qe.spring.data.model.Cat;

public interface CatRepository extends CrudRepository<Cat, Long> {

    //This is for regression test for https://github.com/quarkusio/quarkus/pull/9192
    @Query(value = "SELECT c.distinctive FROM Cat c where c.id = :id")
    boolean customFindDistinctivePrimitive(@Param("id") Long id);

    //This is for regression test for https://github.com/quarkusio/quarkus/pull/9192
    @Query(value = "SELECT c.distinctive FROM Cat c where c.id = :id")
    Boolean customFindDistinctiveObject(@Param("id") Long id);

    @Query(value = "SELECT c FROM Cat c where c.deathReason = :reason")
    List<Cat> findCatsByDeathReason(@Param("reason") String reason);
}
