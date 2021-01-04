package io.quarkus.qe.spring.data;

import io.quarkus.qe.spring.data.model.Cat;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CatRepository extends CrudRepository<Cat, Long> {

    // issue 9192
    @Query(value = "SELECT c.distinctive FROM Cat c where c.id = :id")
    boolean customFindDistinctivePrimitive(@Param("id") Long id);

    // issue 9192
    @Query(value = "SELECT c.distinctive FROM Cat c where c.id = :id")
    Boolean customFindDistinctiveObject(@Param("id") Long id);

    @Query(value = "SELECT c FROM Cat c where c.deathReason = :reason")
    List<Cat> findCatsByDeathReason(@Param("reason") String reason);
}
