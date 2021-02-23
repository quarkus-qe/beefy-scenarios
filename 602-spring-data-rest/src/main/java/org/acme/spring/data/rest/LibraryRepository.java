package org.acme.spring.data.rest;

import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

public interface LibraryRepository extends CrudRepository<Library, Long> {
    @RestResource(path = "id")
    Optional<Library> findById(ID id);
}