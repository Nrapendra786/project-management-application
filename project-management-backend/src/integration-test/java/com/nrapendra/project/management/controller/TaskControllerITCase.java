package com.nrapendra.project.management.controller;

import com.nrapendra.project.management.model.Task;
import com.nrapendra.project.management.repository.ScrumRepository;
import com.nrapendra.project.management.response.TaskResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class TaskControllerITCase extends CommonITCase {

    private String baseURL;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScrumRepository scrumRepository;

    private HttpEntity<String> entity;

    @BeforeEach
    public void setUp(){
        baseURL = "http://localhost:" + port;
        log.info("port is {}",port);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,"application/json");
        entity = new HttpEntity<>("parameters", headers);
    }

    @Test
    public void whenGetAllTasks_thenReceiveSingleTask(){

        //given
        Task task = saveSingleTask();

        //when
        ResponseEntity<TaskResponse> response = this.restTemplate.exchange(
                baseURL + "/tasks/",
                HttpMethod.GET,
                entity,
                TaskResponse.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
      //  assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    public void whenGetSingleTaskById_thenReceiveSingleTask(){

        //given
        Task task = saveSingleTask();

        //when
        ResponseEntity<Task> response = this.restTemplate.exchange(
                baseURL + "/tasks/" + task.getId(),
                HttpMethod.GET,
                entity,
                Task.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    public void whenPostSingleTask_thenItIsStoredInDb(){

        //given
        Task task = createSingleTask();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,"application/json");

        //when
        ResponseEntity<Task> response = this.restTemplate.exchange(
                baseURL + "/tasks/",
                HttpMethod.POST,
                new HttpEntity<>(convertTaskToDTO(task), headers),
                Task.class);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // check response Task
        Task responseTask = response.getBody();
        assertNotNull(responseTask.getId());
        assertEquals(task.getTitle(), responseTask.getTitle());
        assertEquals(task.getDescription(), responseTask.getDescription());
        assertEquals(task.getColor(), responseTask.getColor());
        assertEquals(task.getStatus(), responseTask.getStatus());

        // check saved Task in db
        Task savedTask = findTaskInDbById(responseTask.getId()).get();
        assertEquals(responseTask.getId(), savedTask.getId());
        assertEquals(task.getTitle(), savedTask.getTitle());
        assertEquals(task.getDescription(), savedTask.getDescription());
        assertEquals(task.getColor(), savedTask.getColor());
        assertEquals(task.getStatus(), savedTask.getStatus());
    }

    @Test
    public void whenPostSingleTaskWithScrumAssignment_thenItIsStoredInDb(){

        //given
        Task task = createSingleTask();
        saveSingleRandomScrum();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,"application/json");

        //when
        ResponseEntity<Task> response = this.restTemplate.exchange(
                baseURL + "/tasks/",
                HttpMethod.POST,
                new HttpEntity<>(convertTaskToDTO(task), headers),
                Task.class);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // check response Task
        Task responseTask = response.getBody();
        assertNotNull(responseTask.getId());
        assertEquals(task.getTitle(), responseTask.getTitle());
        assertEquals(task.getDescription(), responseTask.getDescription());
        assertEquals(task.getColor(), responseTask.getColor());
        assertEquals(task.getStatus(), responseTask.getStatus());

        // check saved Task in db
        Task savedTask = findTaskInDbById(responseTask.getId()).get();
        assertEquals(responseTask.getId(), savedTask.getId());
        assertEquals(task.getTitle(), savedTask.getTitle());
        assertEquals(task.getDescription(), savedTask.getDescription());
        assertEquals(task.getColor(), savedTask.getColor());
        assertEquals(task.getStatus(), savedTask.getStatus());
    }

    @Test
    public void whenPutSingleTask_thenItIsUpdated(){

        //given
        Task task = saveSingleTask();
        task.setTitle(task.getTitle() + " Updated");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,"application/json");

        //when
        ResponseEntity<Task> response = this.restTemplate.exchange(
                baseURL + "/tasks/" + task.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(convertTaskToDTO(task), headers),
                Task.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task.getTitle(), findTaskInDbById(task.getId()).get().getTitle());
    }

    @Test
    public void whenDeleteSingleTaskById_thenItIsDeletedFromDb(){

        //given
        Task task = saveSingleTask();

        //when
        ResponseEntity<String> response = this.restTemplate.exchange(
                baseURL + "/tasks/" + task.getId(),
                HttpMethod.DELETE,
                entity,
                String.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(String.format("Task with id: %d was deleted", task.getId()), response.getBody());
        assertFalse(findTaskInDbById(task.getId()).isPresent());
    }
}
