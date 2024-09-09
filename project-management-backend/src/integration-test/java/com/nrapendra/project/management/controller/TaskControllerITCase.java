package com.nrapendra.project.management.controller;

import com.nrapendra.project.management.repository.ScrumRepository;
import com.nrapendra.project.management.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @BeforeEach
    public void setUp(){
        baseURL = "http://localhost:" + port;
        log.info("port is {}",port);
    }

    @Test
    @Disabled
    public void whenGetAllTasks_thenReceiveSingleTask(){

        //given
        Task task = saveSingleTask();

        //when
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<Task[]> response = this.restTemplate.exchange(
                baseURL + "/tasks/",
                HttpMethod.GET,
                entity,
                Task[].class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
      //  assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    @Disabled
    public void whenGetSingleTaskById_thenReceiveSingleTask(){

        //given
        Task task = saveSingleTask();

        //when
        ResponseEntity<Task> response = this.restTemplate.exchange(
                baseURL + "tasks/" + task.getId(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Task.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    @Disabled
    public void whenPostSingleTask_thenItIsStoredInDb(){

        //given
        Task task = createSingleTask();

        //when
        ResponseEntity<Task> response = this.restTemplate.exchange(
                baseURL + "tasks/",
                HttpMethod.POST,
                new HttpEntity<>(convertTaskToDTO(task), new HttpHeaders()),
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
    @Disabled
    public void whenPostSingleTaskWithScrumAssignment_thenItIsStoredInDb(){

        //given
        Task task = createSingleTask();
        saveSingleRandomScrum();

        //when
        ResponseEntity<Task> response = this.restTemplate.exchange(
                baseURL + "tasks/",
                HttpMethod.POST,
                new HttpEntity<>(convertTaskToDTO(task), new HttpHeaders()),
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
    @Disabled
    public void whenPutSingleTask_thenItIsUpdated(){

        //given
        Task task = saveSingleTask();
        task.setTitle(task.getTitle() + " Updated");

        //when
        ResponseEntity<Task> response = this.restTemplate.exchange(
                baseURL + "tasks/" + task.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(convertTaskToDTO(task), new HttpHeaders()),
                Task.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task.getTitle(), findTaskInDbById(task.getId()).get().getTitle());
    }

    @Test
    @Disabled
    public void whenDeleteSingleTaskById_thenItIsDeletedFromDb(){

        //given
        Task task = saveSingleTask();

        //when
        ResponseEntity<String> response = this.restTemplate.exchange(
                baseURL + "tasks/" + task.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(new HttpHeaders()),
                String.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(String.format("Task with id: %d was deleted", task.getId()), response.getBody());
        assertFalse(findTaskInDbById(task.getId()).isPresent());
    }
}
