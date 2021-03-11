package org.acme.spring.data.rest;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface LibraryRepository extends CrudRepository<Library, Long> {
    @Override
    @RestResource(path = "id")
    Optional<Library> findById(Long id);
}