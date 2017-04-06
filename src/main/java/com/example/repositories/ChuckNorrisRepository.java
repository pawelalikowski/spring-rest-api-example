package com.example.repositories;

import com.example.models.ChuckNorris;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface ChuckNorrisRepository extends CrudRepository<ChuckNorris, Long> {
    Optional<ChuckNorris> findById(Long id);
}
