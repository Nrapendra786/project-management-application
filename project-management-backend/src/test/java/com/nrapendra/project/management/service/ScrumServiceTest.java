package com.nrapendra.project.management.service;

import com.nrapendra.project.management.model.Scrum;
import com.nrapendra.project.management.repository.ScrumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScrumServiceTest {

    ScrumService scrumService;

    @Mock
    ScrumRepository scrumRepository;

    @BeforeEach
    public void init() {
        scrumService = new ScrumServiceImpl(scrumRepository);
    }

    @Test
    public void when2ScrumsInDatabase_thenGetListWithAllOfThem() {
        //given
        mockScrumInDatabase(2);

        //when
        List<Scrum> scrums = scrumService.getAllScrumBoards();

        //then
        assertEquals(2, scrums.size());
    }

    private void mockScrumInDatabase(int scrumCount) {
        when(scrumRepository.findAll())
                .thenReturn(createScrumList(scrumCount));
    }

    private List<Scrum> createScrumList(int scrumCount) {
        List<Scrum> scrums = new ArrayList<>();
        IntStream.range(0, scrumCount)
                .forEach(number ->{
                    Scrum scrum = new Scrum();
                    scrum.setId(Long.valueOf(number));
                    scrum.setTitle("Scrum " + number);
                    scrum.setTasks(new ArrayList<>());
                    scrums.add(scrum);
                });
        return scrums;
    }
}
