package ru.demo.soccer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JobFilter {

    /**
     * Job name
     */
    private String name;

    /**
     * Each job has key value pairs
     */
    private String key;
}
