package com.jbd.thymeleaf_start.repos;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.jbd.thymeleaf_start.domain.Media;


public interface MediaRepository extends CrudRepository<Media, Long> {

	Optional<Media> findByName(final String name);
    boolean existsByNameIgnoreCase(String name);

}
