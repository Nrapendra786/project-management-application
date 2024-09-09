package com.nrapendra.project.management.service;

import com.nrapendra.project.management.model.Scrum;
import com.nrapendra.project.management.model.ScrumDTO;
import com.nrapendra.project.management.repository.ScrumRepository;
import com.nrapendra.project.management.config.H2DatabaseConfig4Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { H2DatabaseConfig4Test.class })
public class ScrumServiceITCase {

    @Autowired
    private ScrumRepository scrumRepository;
    private ScrumService scrumService;


    @BeforeEach
    public void init() {
        scrumService = new ScrumServiceImpl(scrumRepository);
    }


    @Test
    public void whenNewScrumCreated_thenScrumIsSavedInDb() {
        //given
        ScrumDTO scrumDTO = ScrumDTO.builder()
                                    .title("Test Scrum")
                                .build();

        //when
        scrumService.saveNewScrum(scrumDTO);

        //then
        List<Scrum> scrums = (List<Scrum>) scrumRepository.findAll();

        assertNotNull(scrums.get(0));
        assertEquals("Test Scrum", scrums.get(0).getTitle());
    }
}
