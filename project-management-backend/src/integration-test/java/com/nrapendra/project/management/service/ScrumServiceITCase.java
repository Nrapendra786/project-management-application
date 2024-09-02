package com.nrapendra.project.management.service;

import com.nrapendra.project.management.model.Scrum;
import com.nrapendra.project.management.model.ScrumDTO;
import com.nrapendra.project.management.repository.ScrumRepository;
import com.nrapendra.project.management.config.H2DatabaseConfig4Test;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { H2DatabaseConfig4Test.class })
public class ScrumServiceITCase {

    @Autowired
    private ScrumRepository scrumRepository;
    private ScrumService scrumService;


    @Before
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
