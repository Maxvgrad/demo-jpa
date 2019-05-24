package ru.demo.jpa.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.demo.jpa.dto.EmployeeFilter;
import ru.demo.jpa.entities.Employee;
import ru.demo.jpa.entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

    private final EntityManager entityManager;

    public List<Employee> search(EmployeeFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Employee> c = cb.createQuery(Employee.class);

        Root<Employee> emp = c.from(Employee.class);
        c.select(emp);
        c.distinct(true);

        Join<Employee, Project> project = emp.join("projects", JoinType.LEFT);
        List<Predicate> criteria = new ArrayList<>();

        if (StringUtils.isNotBlank(filter.getName())) {
            ParameterExpression<String> parameter = cb.parameter(String.class, "name");
            criteria.add(cb.equal(emp.get("name"), parameter));
        }

        if (StringUtils.isNotBlank(filter.getProjectName())) {
            ParameterExpression<String> parameter = cb.parameter(String.class, "project");
            criteria.add(cb.equal(project.get("name"), parameter));
        }

        if (StringUtils.isNotBlank(filter.getDepartmentName())) {
            ParameterExpression<String> parameter = cb.parameter(String.class, "department");
            criteria.add(cb.equal(project.get("department").get("name"), parameter));
        }

        if (StringUtils.isNotBlank(filter.getCity())) {
            ParameterExpression<String> parameter = cb.parameter(String.class, "city");
            criteria.add(cb.equal(project.get("address").get("city"), parameter));
        }

        if (criteria.size() == 0) {
            return Collections.emptyList();
        } else if (criteria.size() == 1) {
            c.where(criteria.get(0));
        } else {
            c.where(cb.and(criteria.toArray(new Predicate[0])));
        }

        TypedQuery<Employee> query = entityManager.createQuery(c);

        if (StringUtils.isNotBlank(filter.getName())) {
            query.setParameter("name", filter.getName());
        }

        if (StringUtils.isNotBlank(filter.getCity())) {
            query.setParameter("city", filter.getCity());
        }

        if (StringUtils.isNotBlank(filter.getDepartmentName())) {
            query.setParameter("department", filter.getDepartmentName());
        }

        if (StringUtils.isNotBlank(filter.getProjectName())) {
            query.setParameter("project", filter.getProjectName());
        }

        return query.getResultList();

    }


}
