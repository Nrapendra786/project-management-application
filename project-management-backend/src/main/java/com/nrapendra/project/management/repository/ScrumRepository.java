package com.nrapendra.project.management.repository;

import com.nrapendra.project.management.model.Scrum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrumRepository extends CrudRepository<Scrum, Long> {

    Optional<Scrum> findByTitle(String title);
}
