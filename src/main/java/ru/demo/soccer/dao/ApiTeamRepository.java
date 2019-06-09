package ru.demo.soccer.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.demo.soccer.entities.ApiTeam;

@Slf4j
@Repository
public class ApiTeamRepository extends JpaGenericDao<ApiTeam, Long> {
    public ApiTeamRepository() {
        super(ApiTeam.class);
    }
}
