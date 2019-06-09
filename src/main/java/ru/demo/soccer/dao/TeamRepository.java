package ru.demo.soccer.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.demo.soccer.entities.Team;

@Slf4j
@Repository
public class TeamRepository extends JpaGenericDao<Team, Long> {
    public TeamRepository() {
        super(Team.class);
    }
}
