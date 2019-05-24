package ru.demo.jpa.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Filter for searching the employee.
 */
@Data
@Builder
public class EmployeeFilter {

    private String name;

    private String departmentName;

    private String city;

    private String projectName;

}
