package org.acme.spring.data.rest;

import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported=false, path = "books", collectionResourceRel = "books")
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

    @RestResource(exported = true)
    Page<Book> findAll(Pageable pageable);

    @RestResource(exported = true)
    List<Book> findByOrderByNameDesc();

    @RestResource(path = "id")
    Optional<Book> findById(ID id);

    @RestResource()
    Book save(Book book);
}
