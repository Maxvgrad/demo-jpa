package ru.demo.soccer.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.demo.soccer.entities.RawResponse;

@Slf4j
@Repository
public class RawResponseRepository extends JpaGenericDao<RawResponse, String> {
    public RawResponseRepository() {
        super(RawResponse.class);
    }
}
