package com._4dconcept.evaluation.repository;

import com._4dconcept.evaluation.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, String> {

    List<Developer> findByProjectId(String projectId);
}
