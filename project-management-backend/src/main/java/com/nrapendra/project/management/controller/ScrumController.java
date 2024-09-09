package com.nrapendra.project.management.controller;

import com.nrapendra.project.management.model.ScrumDTO;
import com.nrapendra.project.management.model.TaskDTO;
import com.nrapendra.project.management.response.ScrumResponse;
import com.nrapendra.project.management.service.ScrumService;
import com.nrapendra.project.management.model.Scrum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/scrums")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ScrumController {

    private final ScrumService scrumService;

    @GetMapping(value= "/",consumes = "application/json",produces = "application/json")
    public ResponseEntity<?> getAllScrums(){
        try {
            var scrumResponse = new ScrumResponse();
            scrumResponse.setScrums(scrumService.getAllScrumBoards());
            return new ResponseEntity<>(scrumResponse, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScrum(@PathVariable Long id){
        try {
            Optional<Scrum> optScrum = scrumService.getScrumById(id);
            if (optScrum.isPresent()) {
                return new ResponseEntity<>(
                        optScrum.get(),
                        HttpStatus.OK);
            } else {
                return noScrumFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getScrumByTitle(@RequestParam String title){
        try {
            Optional<Scrum> optScrum = scrumService.getScrumByTitle(title);
            if (optScrum.isPresent()) {
                return new ResponseEntity<>(
                        optScrum.get(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No scrum found with a title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createScrum(@RequestBody ScrumDTO scrumDTO){
        try {
            return new ResponseEntity<>(
                    scrumService.saveNewScrum(scrumDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateScrum(@PathVariable Long id, @RequestBody ScrumDTO scrumDTO){
        try {
            Optional<Scrum> optScrum = scrumService.getScrumById(id);
            if (optScrum.isPresent()) {
                return new ResponseEntity<>(
                        scrumService.updateScrum(optScrum.get(), scrumDTO),
                        HttpStatus.OK);
            } else {
                return noScrumFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScrum(@PathVariable Long id){
        try {
            Optional<Scrum> optScrum = scrumService.getScrumById(id);
            if (optScrum.isPresent()) {
                scrumService.deleteScrum(optScrum.get());
                return new ResponseEntity<>(
                        String.format("Scrum with id: %d was deleted", id),
                        HttpStatus.OK);
            } else {
                return noScrumFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/{scrumId}/tasks/")
    public ResponseEntity<?> getAllTasksInScrum(@PathVariable Long scrumId){
         try {
            Optional<Scrum> optScrum = scrumService.getScrumById(scrumId);
            if (optScrum.isPresent()) {
                return new ResponseEntity<>(
                        optScrum.get().getTasks(),
                        HttpStatus.OK);
            } else {
                return noScrumFoundResponse(scrumId);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/{scrumId}/tasks/")
    public ResponseEntity<?> createTaskAssignedToScrum(@PathVariable Long scrumId, @RequestBody TaskDTO taskDTO){
        try {
            return new ResponseEntity<>(
                    scrumService.addNewTaskToScrum(scrumId, taskDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    private ResponseEntity<String> errorResponse(){
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> noScrumFoundResponse(Long id){
        return new ResponseEntity<>("No scrum found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
