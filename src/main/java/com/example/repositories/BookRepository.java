package com.example.repositories;

import com.example.models.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findById(Long id);
}
