package com.nrapendra.project.management.controller;

import com.nrapendra.project.management.model.Task;
import com.nrapendra.project.management.model.TaskDTO;
import com.nrapendra.project.management.response.TaskResponse;
import com.nrapendra.project.management.service.TaskService;
//import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private final TaskService taskService;

    @GetMapping(value = "/", consumes = "application/json",produces = "application/json")
    public ResponseEntity<?> getAllTasks(){
        try {
            var taskResponse = new TaskResponse();
            taskResponse.setTasks(taskService.getAllTasks());
            return new ResponseEntity<>(
                    taskResponse,
                    HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id){
        try {
            Optional<Task> optTask = taskService.getTaskById(id);
            if (optTask.isPresent()) {
                return new ResponseEntity<>(
                        optTask.get(),
                        HttpStatus.OK);
            } else {
                return noTaskFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getTaskByTitle(@RequestParam String title){
        try {
            Optional<Task> optTask = taskService.getTaskByTitle(title);
            if (optTask.isPresent()) {
                return new ResponseEntity<>(
                        optTask.get(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No task found with a title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDTO){
        try {
            return new ResponseEntity<>(
                    taskService.saveNewTask(taskDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO){
        try {
            Optional<Task> optTask = taskService.getTaskById(id);
            if (optTask.isPresent()) {
                return new ResponseEntity<>(
                        taskService.updateTask(optTask.get(), taskDTO),
                        HttpStatus.OK);
            } else {
                return noTaskFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id){
        try {
            Optional<Task> optTask = taskService.getTaskById(id);
            if (optTask.isPresent()) {
                taskService.deleteTask(optTask.get());
                return new ResponseEntity<>(String.format("Task with id: %d was deleted", id), HttpStatus.OK);
            } else {
                return noTaskFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    private ResponseEntity<String> errorResponse(){
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> noTaskFoundResponse(Long id){
        return new ResponseEntity<>("No task found with id: " + id, HttpStatus.NOT_FOUND);
    }

}
