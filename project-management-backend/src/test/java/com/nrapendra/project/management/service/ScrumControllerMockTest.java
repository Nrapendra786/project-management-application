package com.nrapendra.project.management.service;

import com.nrapendra.project.management.controller.ScrumController;
import com.nrapendra.project.management.model.Scrum;
import com.nrapendra.project.management.model.Task;
import com.nrapendra.project.management.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ScrumController.class)
public class ScrumControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScrumService scrumService;

    @Test
    public void testGetAllStudents() throws Exception {
        //when
        when(scrumService.getAllScrumBoards()).thenReturn(scrumList());

        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/scrums/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private List<Scrum> scrumList() {

        var firstScrum = new Scrum();
        firstScrum.setId(1L);
        firstScrum.setTitle("Basic Backend Scrum");

        var firstTask = new Task();
        firstTask.setColor("RED");
        firstTask.setDescription("TKY JKOP");
        firstTask.setId(1L);
        firstTask.setStatus(TaskStatus.TODO);
        firstTask.setTitle("titleOne");
        firstScrum.addTask(firstTask);

        return List.of(firstScrum);
    }

}
