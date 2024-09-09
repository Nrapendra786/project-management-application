package com.nrapendra.project.management.response;

import com.nrapendra.project.management.model.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskResponse {
    private List<Task> tasks;
}
