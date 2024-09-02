package com.nrapendra.project.management.service;

import com.nrapendra.project.management.model.Scrum;
import com.nrapendra.project.management.model.ScrumDTO;
import com.nrapendra.project.management.model.TaskDTO;

import java.util.List;
import java.util.Optional;

public interface ScrumService {

    List<Scrum> getAllScrumBoards();

    Optional<Scrum> getScrumById(Long id);

    Optional<Scrum> getScrumByTitle(String title);

    Scrum saveNewScrum(ScrumDTO scrumDTO);

    Scrum updateScrum(Scrum oldScrum, ScrumDTO newScrumDTO);

    void deleteScrum(Scrum scrum);

    Scrum addNewTaskToScrum(Long scrumId, TaskDTO taskDTO);
}
