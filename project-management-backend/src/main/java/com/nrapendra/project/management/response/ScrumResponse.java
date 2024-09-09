package com.nrapendra.project.management.response;

import com.nrapendra.project.management.model.Scrum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScrumResponse {
    private List<Scrum> scrums;
}
