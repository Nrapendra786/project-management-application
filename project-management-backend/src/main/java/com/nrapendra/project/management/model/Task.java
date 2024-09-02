package com.nrapendra.project.management.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "task")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    //@ApiModelProperty(position = 1)
    private Long id;

    @Column(name = "title")
    //@ApiModelProperty(position = 2)
    private String title;

    @Column(name = "description")
    //@ApiModelProperty(position = 3)
    private String description;

    @Column(name = "color")
    //@ApiModelProperty(position = 4)
    private String color;

    @Enumerated(EnumType.STRING)
    //@ApiModelProperty(position = 5)
    private TaskStatus status;
}
