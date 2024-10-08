package com.nrapendra.project.management.controller;

import com.nrapendra.project.management.ProjectManagementApplication;
import com.nrapendra.project.management.model.Scrum;
import com.nrapendra.project.management.model.Task;
import com.nrapendra.project.management.response.ScrumResponse;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProjectManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ScrumControllerITCase extends CommonITCase {

    private String baseURL;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpEntity<String> entity;

    @BeforeEach
    public void setUp() {
        baseURL = "http://localhost:" + port;
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,"application/json");
        entity = new HttpEntity<>("parameters", headers);
    }

    @Test
    public void whenGetAllscrums_thenReceiveSingleScrum() {

        //given
        saveSingleRandomScrum();

        //when
        ResponseEntity<ScrumResponse> response = this.restTemplate.exchange(
                baseURL + "/scrums/",
                HttpMethod.GET,
                entity,
                ScrumResponse.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenGetSingleScrumById_thenReceiveSingleScrum() {

        //given
        Scrum scrum = saveSingleRandomScrum();

        //when
        ResponseEntity<Scrum> response = this.restTemplate.exchange(
                baseURL + "/scrums/" + scrum.getId(),
                HttpMethod.GET,
                entity,
                Scrum.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scrum.getId(), response.getBody().getId());
        assertEquals(scrum.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void whenGetAllTasksForScrumById_thenReceiveTasksList() {

        //given
        Scrum scrum = saveSingleScrumWithOneTask();

        //when
        ResponseEntity<TaskResponse> response = this.restTemplate.exchange(
                baseURL + "/scrums/" + scrum.getId() + "/tasks/",
                HttpMethod.GET,
                entity,
                TaskResponse.class);

          //then
          assertEquals(HttpStatus.OK, response.getStatusCode());
          assertEquals(scrum.getTasks().get(0).getId(), Objects.requireNonNull(Objects.requireNonNull(response.getBody()).getTasks().get(0).getId()));
          assertEquals(scrum.getTasks().get(0).getTitle(), Objects.requireNonNull(response.getBody()).getTasks().get(0).getTitle());
          assertEquals(scrum.getTasks().get(0).getDescription(), Objects.requireNonNull(response.getBody()).getTasks().get(0).getDescription());
          assertEquals(scrum.getTasks().get(0).getColor(), Objects.requireNonNull(response.getBody()).getTasks().get(0).getColor());
    }

    @Test
    public void whenGetSingleScrumByTitle_thenReceiveSingleScrum() {

        //given
        Scrum scrum = saveSingleRandomScrum();

        //when
        ResponseEntity<Scrum> response = this.restTemplate.exchange(
                baseURL + "/scrums?title=" + scrum.getTitle(),
                HttpMethod.GET,
                entity,
                Scrum.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scrum.getId(), response.getBody().getId());
        assertEquals(scrum.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void whenPostSingleScrum_thenItIsStoredInDb() {

        //given
        Scrum scrum = createSingleScrum();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,"application/json");

        //when
        ResponseEntity<Scrum> response = this.restTemplate.exchange(
                baseURL + "/scrums/",
                HttpMethod.POST,
                new HttpEntity<>(convertScrumToDTO(scrum), headers),
                Scrum.class);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // check response Scrum
        Scrum responseScrum = response.getBody();
        assertNotNull(responseScrum.getId());
        assertEquals(scrum.getTitle(), responseScrum.getTitle());

        // check Scrum saved in db
        Scrum savedScrum = findScrumInDbById(responseScrum.getId()).get();
        assertEquals(scrum.getTitle(), savedScrum.getTitle());
    }

    @Test
    public void whenPostSingleTaskToAlreadyCreatedScrum_thenItIsStoredInDbAndAssignedToScrum() {

        //given
        Scrum scrum = saveSingleRandomScrum();
        Task task = createSingleTask();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,"application/json");

        //when
        ResponseEntity<Scrum> response = this.restTemplate.exchange(
                baseURL + "/scrums/" + scrum.getId() + "/tasks/",
                HttpMethod.POST,
                new HttpEntity<>(convertTaskToDTO(task), headers),
                Scrum.class);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // check response Scrum
        Scrum responseScrum = response.getBody();
        assertNotNull(responseScrum.getId());
        assertEquals(scrum.getTitle(), responseScrum.getTitle());
        assertTrue(responseScrum.getTasks().size() == 1);

        Task responseTask = responseScrum.getTasks().get(0);
        // check response Task
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
    public void whenPutSingleScrum_thenItIsUpdated() {

        //given
        Scrum scrum = saveSingleRandomScrum();
        scrum.setTitle(scrum.getTitle() + " Updated");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,"application/json");

        //when
        ResponseEntity<Scrum> response = this.restTemplate.exchange(
                baseURL + "/scrums/" + scrum.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(convertScrumToDTO(scrum), headers),
                Scrum.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scrum.getTitle(), findScrumInDbById(scrum.getId()).get().getTitle());
    }

    @Test
    public void whenDeleteSingleScrumById_thenItIsDeletedFromDb() {

        //given
        Scrum scrum = saveSingleRandomScrum();

        //when
        ResponseEntity<String> response = this.restTemplate.exchange(
                baseURL + "/scrums/" + scrum.getId(),
                HttpMethod.DELETE,
                entity,
                String.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(String.format("Scrum with id: %d was deleted", scrum.getId()), response.getBody());
        assertFalse(findScrumInDbById(scrum.getId()).isPresent());
    }
}
