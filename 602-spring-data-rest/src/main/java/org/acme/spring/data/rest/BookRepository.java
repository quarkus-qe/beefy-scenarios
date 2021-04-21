package org.acme.spring.data.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = false, path = "books", collectionResourceRel = "books")
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

    @Override
    @RestResource(exported = true)
    Page<Book> findAll(Pageable pageable);

    @RestResource(exported = true)
    List<Book> findByOrderByNameDesc();

    @Override
    @RestResource(path = "id")
    Optional<Book> findById(Long id);

    @Override
    @RestResource()
    Book save(Book book);
}
